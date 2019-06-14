package com.whyxs.service.blog;

import com.baomidou.mybatisplus.service.IService;
import com.whyxs.common.bean.entity.BlogArticle;

import java.util.List;

/**
 *
 * @author whyxs
 * @create 2019/5/10
 * @since 1.0.0
 */
public interface ArticleService extends IService<BlogArticle> {

    void save(BlogArticle article, List<String> tagIds);

    void changeSomeStatus(String id,String key,String val);

}
 

