package com.wangy.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.google.common.base.Strings;
import com.wangy.common.model.BaseEntity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author wangy
 * @version 1.0
 * @date 2021/1/17 / 14:39
 */
public class CodeGenerator {

    /** 数据库设置*/
    private static String dbURL = "jdbc:mysql://127.0.0.1:3306/my_project?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT";
    private static String driverName = "com.mysql.cj.jdbc.Driver";
    private static String userName = "root";
    private static String password = "123456";
    /** 使用项目路径，配置好生成文件所在的包即可*/
    private static String path = "web-security-demo/src/main/java";
    private static String author = "wangy";


    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入" + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            //移除特殊符号
            if (!Strings.isNullOrEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 全局配置
     */
    static GlobalConfig globalConfigBuilder() {
        return new GlobalConfig()
            .setOutputDir(path)
            .setAuthor(author)
            //生成后是否打开目录
            .setOpen(true)
            //生成baseResultMap
            .setBaseResultMap(true)
            //是否覆盖
            .setFileOverride(true)
            .setActiveRecord(false)
            // XML 二级缓存
            .setEnableCache(false)
            // XML ColumnList
            .setBaseColumnList(false);
    }

    /**
     * dataSource config
     */
    static DataSourceConfig dataSourceConfigBuilder() {
        return new DataSourceConfig()
            .setDbType(DbType.MYSQL)
            .setUrl(dbURL)
            .setDriverName(driverName)
            .setUsername(userName)
            .setPassword(password);
        // .setSchemaName("public");
    }

    /**
     * package config
     */
    static PackageConfig packageConfigBuilder() {
        return new PackageConfig()
            .setParent("com.wangy")
            .setController("controller")
            .setService("service")
            .setServiceImpl("service.impl")
            .setEntity("model.entity")
            .setMapper("service.mapper")
            .setXml("mapper");
        //.setModuleName(moduleName);
    }

    /**
     * self-definition config
     */
    static InjectionConfig injectionConfigBuilder() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // do nothing
            }
        }.setFileOutConfigList(new ArrayList<FileOutConfig>() {{
            add(new FileOutConfig("/templates/mapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return path + ".mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
        }});
    }

    /**
     * StrategyConfig 策略配置
     */
    static StrategyConfig strategyConfigBuilder(String tableName, String tablePrefix) {

        return new StrategyConfig()
            .setNaming(NamingStrategy.underline_to_camel)
            .setColumnNaming(NamingStrategy.underline_to_camel)
            //lombok模式，不生成get set方法
            .setEntityLombokModel(true)
            .setRestControllerStyle(true)
            //指定实体的基类，生成的entity就会实现Serializable接口
            .setSuperEntityClass(BaseEntity.class)
            //指定生成属性时，驼峰转连字符
            .setControllerMappingHyphenStyle(true)
            .setTablePrefix(Arrays.stream(tablePrefix.split(",")).map(s -> s.concat("_")).toArray(String[]::new))
            .setInclude(tableName.split(","))
            //不生成实体类字段上面表字段注解
            .setEntityTableFieldAnnotationEnable(false)
            // 设置填充字段
            .setTableFillList(new ArrayList<TableFill>() {{
                add(new TableFill("create_by", FieldFill.INSERT));
                add(new TableFill("create_time", FieldFill.INSERT));
                add(new TableFill("update_by", FieldFill.UPDATE));
                add(new TableFill("update_time", FieldFill.UPDATE));
            }});
        //.setSuperControllerClass("com.baomidou.ant.common.BaseController")
        //.setSuperEntityColumns("id");//设置实体的基类的列
    }


    /**
     * AutoGenerator
     *
     * @param tableName     表名，多表使用英文逗号（,）分隔； 如: t_order,sys_file,...,test_user
     * @param tablePrefix   表前缀名，多前缀使用英文逗号（,）分隔； 如：t,sys,...,test
     */
    static void generate( String tableName, String tablePrefix) {
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(globalConfigBuilder());
        mpg.setDataSource(dataSourceConfigBuilder());
        mpg.setPackageInfo(packageConfigBuilder());
        mpg.setCfg(injectionConfigBuilder());
        mpg.setStrategy(strategyConfigBuilder(tableName, tablePrefix));
        // template
        mpg.setTemplate(new TemplateConfig().setXml(null));
        // template engine
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static void main(String[] args) {
        String tableName = scanner("表名");
        String tablePrefix = scanner("表前缀");
        String authorInput = scanner("作者(不输入则使用“系统默认”)");
        if (!StringUtils.isBlank(authorInput)) {
            author = authorInput;
        }
        generate(tableName, tablePrefix);
    }

}
