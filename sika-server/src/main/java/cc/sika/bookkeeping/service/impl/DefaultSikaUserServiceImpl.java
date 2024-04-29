package cc.sika.bookkeeping.service.impl;

import cc.sika.bookkeeping.mapper.SikaUserMapper;
import cc.sika.bookkeeping.pojo.po.SikaUser;
import cc.sika.bookkeeping.service.SikaUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service("sikaUserService")
public class DefaultSikaUserServiceImpl extends ServiceImpl<SikaUserMapper, SikaUser> implements SikaUserService{
}
