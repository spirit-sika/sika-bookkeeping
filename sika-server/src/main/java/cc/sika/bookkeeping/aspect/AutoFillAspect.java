package cc.sika.bookkeeping.aspect;

import cc.sika.bookkeeping.annotation.AutoFill;
import cc.sika.bookkeeping.constant.AuthenticationConstant;
import cc.sika.bookkeeping.constant.AutoFillMethodConstant;
import cc.sika.bookkeeping.constant.OperationType;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* cc.sika.bookkeeping.mapper.*.*(..))" +
            "&& @annotation(cc.sika.bookkeeping.annotation.AutoFill)")
    public void autoFillPointcut(){}

    @Before("autoFillPointcut()")
    public void beforeAutoFillPointcut(JoinPoint joinPoint){
        log.info("开始字段填充 {}", joinPoint);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 切点表达式需要符合具备AutoFill注解, 此处不会空指针
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();
        // 通过标注注解的方法获取参数列表
        Object[] args = joinPoint.getArgs();
        // 切面参数异常
        if (Objects.isNull(args) || args.length == 0) {
            return;
        }
        // mapper方法接收一个即将插入数据库的信息对象, 参数列表第一个为方法接收的PO对象
        Object po  = args[0];
        // 反射获取po类方法进行数据插入
        LocalDateTime now = LocalDateTime.now();
        try {
            Method createBy = po.getClass()
                    .getDeclaredMethod(AutoFillMethodConstant.SET_CREATE_BY, String.class);
            Method updateBy = po.getClass()
                    .getDeclaredMethod(AutoFillMethodConstant.SET_UPDATE_BY, String.class);

            Method createTime = po.getClass()
                    .getDeclaredMethod(AutoFillMethodConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method updateTime = po.getClass()
                    .getDeclaredMethod(AutoFillMethodConstant.SET_UPDATE_TIME, LocalDateTime.class);

            String operatorName = AutoFillMethodConstant.UNKNOWN;
            // 如果是注册则操作, 设置为 po的nickname属性
            try {
                Field nicknameField = po.getClass().getDeclaredField("nickname");
                nicknameField.setAccessible(true);
                operatorName = (String) nicknameField.get(po);
            } catch (NoSuchFieldException | SecurityException e) {
                log.warn("当前操作非注册操作, 反射获取PO的nickname字段失败, 尝试在登录会话获取当前用户昵称");
                // 非注册操作用户已经登录, 在登录会话中获取当前用户
                Object currentUser = null;
                try {
                    currentUser = StpUtil.getSession()
                            .get(AuthenticationConstant.CURRENT_USER);
                } catch (NullPointerException nullPointerException) {
                    log.warn("登录会话获取当前用户信息失败, {}", nullPointerException.getMessage());
                }
                if (!Objects.isNull(currentUser)) {
                    operatorName = ((SikaUser)currentUser).getNickname();
                }
            }
            // 填充字段
            createBy.invoke(po, operatorName);
            updateBy.invoke(po, operatorName);
            createTime.invoke(po, now);
            updateTime.invoke(po, now);
        } catch (NoSuchMethodException e) {
            log.error("反射获取PO方法失败, {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException invocationException) {
            log.error("反射填充公共字段发生异常, {}", invocationException.getMessage());
            throw new RuntimeException(invocationException);
        }
    }
}
