package org.air.bigearth.apps.file.service.impl;

import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.FileStrategyConstants;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.RemoteFileConstants;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.domain.ImageMetadata;
import org.air.bigearth.apps.file.domain.MultipartFileParam;
import org.air.bigearth.apps.file.exception.FileException;
import org.air.bigearth.apps.file.mapper.AttachmentMapper;
import org.air.bigearth.apps.file.service.IAttachmentService;
import org.air.bigearth.apps.file.service.IFileStrategyService;
import org.air.bigearth.apps.file.util.AttachmentUtil;
import org.air.bigearth.apps.file.util.DeleteFileUtil;
import org.air.bigearth.apps.file.util.FileMD5Util;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.annotation.Target;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 附件 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
@Service
@Transactional(readOnly = true)
public class AttachmentServiceImpl implements IAttachmentService {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    /**
     * 这个必须与前端设定的值一致
     */
    @Value("${breakpoint.upload.chunkSize}")
    private long CHUNK_SIZE;

    @Value("${breakpoint.upload.dir}")
    private String finalDirPath;

    @Resource
    private IFileStrategyService fileStrategysService;
    @Resource
    private AttachmentMapper attachmentMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean insertDto(Attachment dto) throws BaseException {
        return this.attachmentMapper.insertDto(dto);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateDto(Attachment dto) throws BaseException {
        return false;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean deleteByIds(String[] ids) throws BaseException {
        return false;
    }

    @Override
    public Attachment getById(String id) throws BaseException {
        return (Attachment) this.attachmentMapper.getById(id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Attachment> searchByParamsPage(Map<String, Object> params) throws BaseException {
        return (List<Attachment>) attachmentMapper.searchByParamsPage(params);
    }

    /**
     * 根据文件策略编码获取文件策略
     *
     * @param strategyCode 文件策略编码
     * @return
     * @throws BaseException
     */
    @Override
    public FileStrategy getFileStrategyCode(String strategyCode) throws BaseException {
        FileStrategy fileStrategyDTO = null;
        if (StringUtils.isEmpty(strategyCode)) {
            fileStrategyDTO = fileStrategysService.getByCode(FileStrategyConstants.DEFAULT);
        } else {
            fileStrategyDTO = fileStrategysService.getByCode(strategyCode);
        }
        return fileStrategyDTO;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> uploadAttachment(HttpServletRequest request, MultipartFile[] files) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Attachment> list = new ArrayList<Attachment>();

        Map paramMap = (Map) request.getAttribute("paramMap");

        String strategyCode = FileStrategyConstants.DEFAULT;
        // 文件默认存储状态
        String saveState = request.getParameter("saveState");
        String doPage = request.getParameter("doPage");
        // 是否更新文件，等于'1' 即删除原始文件
        String isUpdate = request.getParameter("isUpdate");
        int filepathRule = 0;
        FileStrategy fileStrategyDTO = null;
        try {
            // 根据code查询文件策略信息
            fileStrategyDTO = getFileStrategyCode(strategyCode);
            filepathRule = Integer.valueOf(fileStrategyDTO.getFilepathRule());
        } catch (Exception e1) {
            logger.error(e1.getMessage(), e1);
            result.put("success", false);
            result.put("msg", e1.getMessage());
        }

        String uploadPath = "";
        try {
            // 构造临时路径来存储上传的文件, 这个路径相对当前应用的目录
            File file = AttachmentUtil.getTargetFilePath(fileStrategyDTO.getBasePath(), fileStrategyDTO.getFilepathRule(), null);
            if (file != null) {
                uploadPath = file.toString();
                result.put("uploadPath", uploadPath);
                FileUtils.createFileAndWritePermission(uploadPath);
            } else {
                uploadPath = fileStrategyDTO.getBasePath();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.put("success", false);
            result.put("msg", e.getMessage());
            return result;
        }

        long startTime = System.currentTimeMillis();
        // 将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            try {
                //将request变成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

                //获取multiRequest 中所有的文件名
                Iterator iter = multiRequest.getFileNames();
                int count = 0;
                for (MultipartFile file : files) {
                    Attachment attachmentDTO = new Attachment();
                    attachmentDTO.setBusiId(request.getParameter("busiId"));
                    attachmentDTO.setBusiType(request.getParameter("busiType"));
                    count++;
                    int maxUploadFileNumber = fileStrategyDTO.getMaxUploadFileNumber();
                    if (count > maxUploadFileNumber) {
                        result.put("success", false);
                        result.put("msg", "上传的文件超过指定最大上传文件数！");
                        return result;
                    }

                    if (file != null) {
                        // 随机id
                        attachmentDTO.setAttachmentId(DataKeyUtil.getDataKey());
                        String originalFileName = file.getOriginalFilename();
                        if (file.isEmpty() && !StringUtil.isNotNullOrBlank(originalFileName)) {
                            result.put("success", false);
                            result.put("msg", "请选择文件！");
                            return result;
                        }
                        if (!StringUtil.isNotNullOrBlank(originalFileName)) {
                            originalFileName = request.getParameter("fileName");
                        }

                        String fileSuffix = AttachmentUtil.getFileSuffix(originalFileName);

                        String filePath = uploadPath + File.separator + originalFileName;
                        File localFtpFile = new File(filePath);
                        String fileName = null;
                        if (localFtpFile.exists() && StringUtil.isNotNullOrBlank(fileSuffix)) {
                            fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + DataKeyUtil.generateShortUuid() + "." + fileSuffix;
                            filePath = uploadPath + File.separator + fileName;
                        } else if (localFtpFile.exists() && StringUtil.isEmpty(fileSuffix)) {
                            fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "_" + DataKeyUtil.generateShortUuid();
                            filePath = uploadPath + File.separator + fileName;
                        } else {
                            fileName = originalFileName;
                        }

                        /**
                         * 上传限制 start
                         * 如果是文件上传页面请求，则不验证上传限制
                         * doPage不为空时为文件上传页面请求
                         */
                        if (doPage == null || doPage.equals("undefined")) {
                            // 文件允许类型限制
                            String uploadfileType = fileStrategyDTO.getUploadfileType();
                            if (StringUtil.isNotNullOrBlank(uploadfileType) && !uploadfileType.equals("*")) {
                                if (!uploadfileType.contains(fileSuffix)) {
                                    result.put("success", false);
                                    result.put("msg", "上传的文件格式不正确！");
                                    return result;
                                }
                            }
                            //不允许上传的文件类型限制
                            String forbidFileType = fileStrategyDTO.getForbidfileType();
                            if (StringUtil.isNotNullOrBlank(forbidFileType)) {
                                if (StringUtil.isNotNullOrBlank(fileSuffix) && forbidFileType.contains(fileSuffix)) {
                                    result.put("success", false);
                                    result.put("msg", "上传的文件格式为禁止上传类型！");
                                    return result;
                                }
                            }
                            // 大小限制,单位M
                            float fileSize = (float) file.getSize() / (float) 1024 / (float) 1024;
                            int fileSizeLimit = fileStrategyDTO.getFilesizeLimit();
                            if (fileSizeLimit > 0) {
                                if (fileSize > fileSizeLimit) {
                                    result.put("success", false);
                                    result.put("msg", "上传的单个文件大小不能超过" + fileSizeLimit + "M！");
                                    return result;
                                }
                            }
                            // 加入文件名单引号限制
                            if (originalFileName.indexOf("'") != -1) {
                                result.put("success", false);
                                result.put("msg", "上传的文件名称中不能包含单引号！");
                                return result;
                            }
                        }
                        //上传限制 end

                        //上传
                        file.transferTo(new File(filePath));

                        // 前端传递md5
                        String fileMD5 = request.getParameter("md5Code");
                        if (StringUtil.isEmpty(fileMD5) && !StringUtil.isEmpty(paramMap.get("md5Code"))) {
                            fileMD5 = paramMap.get("md5Code").toString();
                        }
                        // 后端计算md5
                        String calculateMd5 = MD5Class.getMD5Three(filePath);
                        if (!fileMD5.equals(calculateMd5)) {
                            DeleteFileUtil.deleteFile(filePath);
                            result.put("success", false);
                            result.put("msg", "上传文件为伪md5码！");
                            return result;
                        }

                        File inputFile = new File(filePath);
                        File outputFile = null;
                        //加密方式
                        String encryptType = fileStrategyDTO.getEncryptType();

                        //是否压缩
                        int compressedFlag = Integer.valueOf(fileStrategyDTO.getCompressedFlag());
                        attachmentDTO.setCompressedFlag(compressedFlag);
                        File zipFile = null;
                        zipFile = AttachmentUtil.zipFile(attachmentDTO, filePath, uploadPath);
                        if (zipFile != null) {
                            fileName = zipFile.getName();
                            filePath = zipFile.getPath();
                        }//压缩end


                        if (filepathRule == 4) {
                            File attachmentFile;
                            if (outputFile != null) {
                                attachmentFile = outputFile;
                            } else {
                                attachmentFile = inputFile;
                            }
                            InputStream inputStream = new FileInputStream(attachmentFile);
                            byte[] data = new byte[]{};
                            // 将文件保存到字节数组中
                            data = inputStreamToByte(inputStream);
                            attachmentDTO.setAttachment(data);
                            outputFile.delete();
                            inputFile.delete();
                        }

                        attachmentDTO.setOriginalFileName(originalFileName);
                        attachmentDTO.setFileName(fileName);
                        attachmentDTO.setEncryptedType(encryptType);
                        //根路径
                        attachmentDTO.setBasePath(fileStrategyDTO.getBasePath());
                        //后缀
                        attachmentDTO.setSuffix(fileSuffix);
                        //文件策略id
                        attachmentDTO.setStrategyId(fileStrategyDTO.getStrategyId());
                        //上传时间
                        attachmentDTO.setUploadTime(DateUtil.getCurrentTime());

                        attachmentDTO.setFilePath(filePath);
                        //attachmentDTO.setFileSize(Integer.valueOf((int) (file.getSize() / 1024)));
                        User user = (User) SecurityUtils.getSubject().getPrincipal();
                        attachmentDTO.setCreateUserId(user != null ? user.getId() : "");
                        // 设置存储状态
                        if (saveState != null && !"".equals(saveState.trim()) && attachmentDTO.getSaveState() == null) {
                            attachmentDTO.setSaveState(Integer.valueOf(saveState));
                        }
                        attachmentDTO.setFileCode(calculateMd5);

                        ImageMetadata metadata = new ImageMetadata();
                        metadata.setDatakey(DataKeyUtil.getDataKey());
                        metadata.setUploadSource("custom");
                        metadata.setCreateTime(DateUtil.getCurrentTime());
                        JSONObject metadataJson = JSONObject.fromObject(metadata);
                        attachmentDTO.setUploadMetadata(metadataJson.toString());
                        list.add(attachmentDTO);

                        if (isUpdate != null && !isUpdate.equals("undefined")) {
                            if (isUpdate.equals("1")) {
                                String attachmentId = request.getParameter("attachmentId");
                                result.put("attachmentId", attachmentId);
                                result.put("busiId", request.getParameter("busiId"));
                                attachmentMapper.deleteAttachment(result);
                            }
                        }
                        //将上传文件的信息插入附件表
                        attachmentMapper.insertDto(attachmentDTO);
                        result.put("success", true);
                        result.put("msg", "上传成功！");

                        // 获取UUID及数据集
                        String isExistInfo = AttachmentUtil.checkFileIsExistByFileMd5(calculateMd5);
                        // 推送文件到服务器
                        AttachmentUtil.uploadFileToServer(attachmentDTO, isExistInfo);
                        // 上传后删除文件
                        DeleteFileUtil.delete(filePath);
                    } else {
                        result.put("success", false);
                        result.put("msg", "请选择文件！");
                        return result;
                    }
                }
            } catch (Exception e) {
                logger.error("文件上传失败！" + e.getMessage(), e);
                result.put("success", false);
                result.put("msg", e.getMessage());
                return result;
            }
        }
        long endTime = System.currentTimeMillis();
        result.put("list", list);
        return result;
    }

    /**
     * 将文件保存到字节数组中
     *
     * @param is
     * @return
     * @throws IOException
     */
    private byte[] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bAOutputStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bAOutputStream.write(ch);
        }
        byte data[] = bAOutputStream.toByteArray();
        bAOutputStream.close();
        return data;
    }

    @Override
    public String getEncryptType(String cryptType) {
        if (cryptType.equals("1")) {
            cryptType = "AES";
        } else if (cryptType.equals("2")) {
            cryptType = "DES";
        }
        return cryptType;
    }

    @Override
    public List<Attachment> attachmentParams(Map<String, Object> params) {
        return attachmentMapper.attachmentParams(params);
    }

    /**
     * 根据id删除附件
     *
     * @param attachmentIds attachmentIds(附件id,多个以逗号隔开)
     * @return Map<String       ,       Object> (返回类型)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteAttachment(String attachmentIds) {
        attachmentMapper.deleteAttachmentIds(attachmentIds.split(","));
    }

    /**
     * 文件解压缩
     *
     * @param AttachmentDTO
     * @return File (返回类型)
     */
    @Override
    public File unzip(Attachment AttachmentDTO) {
        String filePath = AttachmentDTO.getFilePath();
        //文件解压缩
        if (StringUtil.isNotNullOrBlank(AttachmentDTO.getCompressedFlag()) && AttachmentDTO.getCompressedFlag() == 1) {
            String outFileStr = filePath.replaceAll("/", "\\").substring(0, filePath.lastIndexOf("\\")) + "\\";
            //解压组件
            ZipKit.unzip(new File(filePath), new File(outFileStr));
            String fileName = AttachmentDTO.getFileName();
            fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1) + AttachmentDTO.getSuffix();
            File outFile = new File(outFileStr + fileName);
            if (outFile.exists()) {
                return outFile;
            } else {
                return new File(filePath);
            }
        }//文件解压缩end
        return new File(filePath);
    }

    /**
     * 根据批量附件ID查询附件
     *
     * @return List<FileStrategyDTO> (返回类型)
     */
    @Override
    public List<Attachment> selectAttListByIds(String[] attIdArray) {
        return this.attachmentMapper.selectAttachmentListByIds(Arrays.asList(attIdArray));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void uploadFileRandomAccessFile(MultipartFileParam param) throws IOException {
        String fileName = param.getName();
        String tempDirPath = finalDirPath + param.getMd5();
        String tempFileName = fileName + "_tmp";
        File tmpDir = new File(tempDirPath);
        File tmpFile = new File(tempDirPath, tempFileName);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }

        RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
        long offset = CHUNK_SIZE * param.getChunk();
        //定位到该分片的偏移量
        accessTmpFile.seek(offset);
        //写入该分片数据
        accessTmpFile.write(param.getFile().getBytes());
        // 释放
        accessTmpFile.close();

        boolean isOk = checkAndSetUploadProgress(param, tempDirPath);
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            System.out.println("upload complete !!" + flag + " name=" + fileName);
        }
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> uploadFileByMappedByteBuffer(MultipartFileParam multipartFileParam, HttpServletRequest request) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();

        String fileName = multipartFileParam.getName();
        FileStrategy fileStrategy = fileStrategysService.getByCode(FileStrategyConstants.DEFAULT);
        File targetPath = AttachmentUtil.getTargetFilePath(fileStrategy.getBasePath(), fileStrategy.getFilepathRule(), multipartFileParam.getMd5());
        AttachmentUtil.createDir(targetPath);
        String uploadDirPath = targetPath != null ? targetPath.toString() : fileStrategy.getBasePath();

        Map<String, Object> requestParams = DataTransform.requestToObjectMap(request);
        requestParams.put("fileStrategy", fileStrategy);
        requestParams.put("uploadDirPath", uploadDirPath);

        String tempFileName = fileName + "_tmp";
        File tmpFile = new File(uploadDirPath, tempFileName);

        RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
        FileChannel fileChannel = tempRaf.getChannel();

        //写入该分片数据
        long offset = CHUNK_SIZE * multipartFileParam.getChunk();
        byte[] fileData = multipartFileParam.getFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
        mappedByteBuffer.put(fileData);

        // 检查是否还有线程在读或写
        FileMD5Util.freedMappedByteBuffer(mappedByteBuffer);
        // 释放
        fileChannel.close();

        //  是否已上传到本地
        boolean isOk = checkAndSetUploadProgress(multipartFileParam, uploadDirPath);
        if (isOk) {
            boolean flag = renameFile(tmpFile, fileName);
            // 前台传递与后台计算MD5值比较
            String localFile = targetPath + File.separator + fileName;
            String calculateMD5 = FileMD5Util.getFileMD5(new File(localFile));
            if (!StringUtil.isEmpty(multipartFileParam.getMd5()) && !multipartFileParam.getMd5().equals(calculateMD5)) {
                result.put("success", false);
                result.put("msg", "上传文件为伪MD5码！");
                return result;
            }
            logger.info("upload complete to local !!" + flag + " name=" + fileName);
            addAttachment(multipartFileParam, requestParams);
            logger.info("upload complete to server !!" + flag + " name=" + fileName);

            // 上传后删除本地文件
            DeleteFileUtil.delete(localFile);
            String confFile = targetPath + File.separator + fileName + ".conf";
            DeleteFileUtil.delete(confFile);
        }

        return result;
    }



    /**
     * 插入附件信息到数据库，并推送附件信息、文件到数据服务器
     *
     * @param multipartFileParam 大文件分片上传参数
     * @param requestParams 前端请求参数
     * @return
     */
    public Map<String, Object> addAttachment(MultipartFileParam multipartFileParam, Map<String, Object> requestParams) {
        Map<String, Object> result = new HashMap<String, Object>();
        String fileMd5 = multipartFileParam.getMd5();
        String fileName = multipartFileParam.getName();
        String fileSize = requestParams.get("fileSize").toString();
        String fileShare = requestParams.get("fileShare").toString();
        String uploadPath = requestParams.get("uploadDirPath").toString();
        String uploadMetadata = requestParams.get("uploadMetadata").toString();
        FileStrategy fileStrategy = (FileStrategy) requestParams.get("fileStrategy");
        User user = ShiroUtil.getSysUser();
        Attachment attachment = getAttachment(fileMd5, fileName, fileSize, user, fileStrategy, uploadPath, uploadMetadata, fileShare);
        String fileIsExistStr = AttachmentUtil.checkFileIsExistByFileMd5(fileMd5);
        String isUploadToServer = AttachmentUtil.uploadFileToServer(attachment, fileIsExistStr);
        result.put("isUploadToServer", isUploadToServer);

        return result;
    }

    /**
     * 获取附件信息
     *
     * @param fileMd5        文件MD5值
     * @param fileName       文件名称
     * @param fileSize       文件大小
     * @param user           用户对象
     * @param fileStrategy   文件策略对象
     * @param uploadPath     文件路径
     * @param uploadMetadata 上传文件元数据
     * @param fileShare      文件共享
     * @return
     */
    public Attachment getAttachment(String fileMd5, String fileName, String fileSize, User user, FileStrategy fileStrategy, String uploadPath, String uploadMetadata, String fileShare) {
        Attachment attachment = new Attachment();
        attachment.setAttachmentId(DataKeyUtil.getDataKey());
        attachment.setFileName(fileName);
        String originalFileName = fileName;
        // 原始文件名
        attachment.setOriginalFileName(originalFileName);
        //根路径
        attachment.setBasePath(fileStrategy.getBasePath());

        String filePath = uploadPath + File.separator + originalFileName;
        attachment.setFilePath(filePath);
        long lfileSize = Long.valueOf(fileSize).longValue();
        attachment.setFileSize(BigInteger.valueOf(lfileSize));
        String fileSuffix = AttachmentUtil.getFileSuffix(originalFileName);
        //后缀
        attachment.setSuffix(fileSuffix);
        //是否压缩
        attachment.setCompressedFlag(Integer.valueOf(fileStrategy.getCompressedFlag()));
        //文件策略id
        attachment.setStrategyId(fileStrategy.getStrategyId());
        //上传时间
        attachment.setUploadTime(DateUtil.getCurrentTime());
        attachment.setCreateUserId(user != null ? user.getId() : "");
        attachment.setFileCode(fileMd5);
        attachment.setUploadMetadata(uploadMetadata);
        // 是否共享
        attachment.setFileShare(fileShare);
        //将上传文件的信息插入附件表
        attachmentMapper.insertDto(attachment);
        return attachment;
    }

    /**
     * 保存或更新附件到数据库中,并推送附件信息、文件到数据服务器
     *
     * @param requestParams
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> saveAttachment(Map<String, String> requestParams) {
        Map<String, Object> result = new HashMap<String, Object>();
        result = saveOrUpdateAttachment(requestParams);
        return result;
    }

    /**
     * 保存或更新附件到数据库中,并推送附件信息、文件到数据服务器
     *
     * @param requestParams 前端请求参数
     * @return
     */
    public Map<String, Object> saveOrUpdateAttachment(Map<String, String> requestParams) {
        Map<String, Object> result = new HashMap<String, Object>();

        String fileMd5 = requestParams.get("md5");
        String fileName = requestParams.get("fileName");
        String fileSize = requestParams.get("fileSize");
        String fileShare = requestParams.get("fileShare");
        String uploadMetadata = requestParams.get("uploadMetadata");
        String fileIsExistStr = requestParams.get("fileIsExistStr");

        Attachment attachment = null;
        User user = ShiroUtil.getSysUser();
        FileStrategy fileStrategy = getFileStrategyCode(FileStrategyConstants.DEFAULT);
        // 构造临时路径来存储上传的文件, 这个路径相对当前应用的目录
        File targetPath = AttachmentUtil.getTargetFilePath(fileStrategy.getBasePath(), fileStrategy.getFilepathRule(), fileMd5);
        if (!targetPath.exists()) {
            targetPath.mkdirs();
            // 当前用户未上传该文件,插入数据
            String uploadPath = targetPath != null ? targetPath.toString() : fileStrategy.getBasePath();
            attachment = getAttachment(fileMd5, fileName, fileSize, user, fileStrategy, uploadPath, uploadMetadata, fileShare);
        } else {
            // 当前用户已上传该文件,更新数据
            Map<String, Object> attachmentMap = new HashMap<String, Object>();
            attachmentMap.put("createUserId", user.getId());
            attachmentMap.put("fileMd5", fileMd5);
            List<Attachment> listAttachments = attachmentMapper.attachmentParams(attachmentMap);
            if (listAttachments != null && listAttachments.size() > 0) {
                attachment = (Attachment) attachmentMapper.getById(listAttachments.get(0).getAttachmentId());
                attachment.setFileName(fileName);
                attachment.setOriginalFileName(fileName);
                attachment.setUpdateTime(DateUtil.getCurrentTime());
                attachment.setUploadMetadata(uploadMetadata);
                attachment.setFileShare(fileShare);
                attachmentMapper.updateDto(attachment);
            }
        }
        String isUploadToServer = AttachmentUtil.uploadFileToServer(attachment, fileIsExistStr);
        result.put("isUploadToServer", isUploadToServer);

        return result;
    }

    /**
     * 检查并修改文件上传进度
     *
     * @param param
     * @param uploadDirPath
     * @return
     * @throws IOException
     */
    private boolean checkAndSetUploadProgress(MultipartFileParam param, String uploadDirPath) throws IOException {
        String fileName = param.getName();
        File confFile = new File(uploadDirPath, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
        //把该分段标记为 true 表示完成
        System.out.println("set part " + param.getChunk() + " complete");
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = org.apache.commons.io.FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            System.out.println("check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            //stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "true");
            //stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName);
            return true;
        } else {
            //if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, param.getMd5())) {
            //    stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, param.getMd5(), "false");
            //}
            //if (stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + param.getMd5())) {
            //    stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + param.getMd5(), uploadDirPath + "/" + fileName + ".conf");
            //}
            return false;
        }
    }

    /**
     * 文件重命名
     *
     * @param toBeRenamed   将要修改名字的文件
     * @param toFileNewName 新的名字
     * @return
     */
    public boolean renameFile(File toBeRenamed, String toFileNewName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!toBeRenamed.exists() || toBeRenamed.isDirectory()) {
            logger.info("File does not exist: " + toBeRenamed.getName());
            return false;
        }
        String p = toBeRenamed.getParent();
        File newFile = new File(p + File.separatorChar + toFileNewName);
        //修改文件名
        return toBeRenamed.renameTo(newFile);
    }

}
