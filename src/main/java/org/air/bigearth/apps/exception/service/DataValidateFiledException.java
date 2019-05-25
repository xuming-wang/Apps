package org.air.bigearth.apps.exception.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.air.bigearth.apps.util.FiledError;
import org.springframework.util.CollectionUtils;

/**
 * 自定义参数校验异常类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class DataValidateFiledException extends RuntimeException {

	private static final long serialVersionUID = 2256477558314496007L;
	 
	private Collection <FiledError> filedErrors=new HashSet<FiledError>();

	public DataValidateFiledException(Collection<FiledError> filedError) {
		super("数据校验失败");
		fillFieldErrors(filedError);
	}

	public DataValidateFiledException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//字符串抛个检验异常
	public DataValidateFiledException(String filedError) {
		super();
		fillFieldError(new FiledError(filedError,"",""));
	}
	
	public DataValidateFiledException(FiledError filedError){
		super("数据检验失败");
		filedErrors.add(filedError);
	}
	
    private final void fillFieldErrors(Collection<FiledError> fieldErrors) {
        if (Objects.nonNull(fieldErrors)) {
        	//如果fieldErrors 不为null
            this.filedErrors.addAll(fieldErrors);
        }
    }
  
    private final void fillFieldError(FiledError fieldError) {
        if (Objects.nonNull(filedErrors)) {
            this.filedErrors.add(fieldError);
        }
    }
    
    /**
     * 重写Throwable的getMessage方法，控制台输出的异常信息就是message，把想输出的信息放在message
     */
    @Override
    public String getMessage() {
        StringBuffer msg = new StringBuffer(super.getMessage());
        //StringBuffer msg = new StringBuffer();
        if (!CollectionUtils.isEmpty(filedErrors)) {
            msg.append(":");
            for (FiledError fieldError : filedErrors) {
            	msg.append("对象"+fieldError.getBeanName()+",");
            	msg.append("字段"+fieldError.getFiledName()+",");
                msg.append(fieldError.getMessage()).append("\n");
            }
        }
        return msg.toString();
    }
}
