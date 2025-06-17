package com.my.mall.shortlink.service.Impl;

import com.my.mall.shortlink.service.UrlTitleService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@Service
public class UrlTitleServiceImpl implements UrlTitleService {
    @Override
    public String getTitle(String url) {
        // 统一使用 Jsoup 处理连接，避免重复请求
        try {
            // 配置 Jsoup 连接参数
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10 * 1000)
                    .get();

            // 提取标题并处理空值
            return document.title() != null ? document.title() : "No title found";

        } catch (MalformedURLException e) {
            return "Invalid URL: " + e.getMessage();
        } catch (IOException e) {
            return "Fetch error: " + e.getMessage();
        }
    }
}
