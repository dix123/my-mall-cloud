package com.my.mall.mbg;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

/**
 * @Author: haole
 * @Date: 2025/4/23
 **/
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mallswarm?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("haole") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("mall-mbg/src/main/java"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.my.mall.mbg") // 设置父包名
//                                .moduleName("mall-mbg") // 设置父包模块名
                                .entity("model")
                                .mapper("dao")
                                .service("service")
                                .serviceImpl("service.impl")
                                .xml("mapper")
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "mall-mbg/src/main/resources")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.entityBuilder()
                                .enableLombok()
                                .enableTableFieldAnnotation()
                                .enableFileOverride()
                                .controllerBuilder()
                                .enableRestStyle()
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
