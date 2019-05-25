package org.air.bigearth.apps.upload;

import org.air.bigearth.apps.AppsApplication;
import org.air.bigearth.apps.upload.dto.FileUploadProgressDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传 控制层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-24
 */
@Controller
public class FileUploadController {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	public static final String PREFIX = "/file";

	/**
	 * 多文件上传
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/batchUpload", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); i++) {
			file = files.get(i);
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
					stream.write(bytes);
					stream.flush();
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				return "上传失败！文件为空值!";
			}
		}
		return "success";
	}

	/**
	 * 文件上传进度
	 *
	 * @param request
	 * @return
	 */
	@GetMapping(value = "/uploadProgress")
	@ResponseBody
	public FileUploadProgressDTO getProgress(HttpServletRequest request){
		HttpSession session = request.getSession();
		FileUploadProgressDTO progressDTO = (FileUploadProgressDTO) session.getAttribute("progressDTO");
		return progressDTO;
	}















}
