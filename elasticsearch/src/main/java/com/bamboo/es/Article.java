package com.bamboo.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;


/**
 * 　@Document 作用在类，标记实体类为文档对象，一般有两个属性
 * 　　　indexName：对应索引库名称
 * 　　　shards：分片数量，默认1
 * 　　　replicas：副本数量，默认1
 */
@Document(indexName = "es_bean")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable  {

    /**
     * @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：
     * 　 type：字段类型，是枚举，默认自动检测属性的类型：FieldType，可以是text、long、short、date、integer、object等
     * 　　　　text：存储数据时候，会自动分词，并生成索引
     * 　　　　keyword：存储数据时候，不会分词建立索引
     * 　　　　Numerical：数值类型，分两类
     * 　　　　　　基本数据类型：long、interger、short、byte、double、float、half_float
     * 　　　　　　浮点数的高精度类型：scaled_float
     * 　　　　　　需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原。
     * 　　　　Date：日期类型
     * 　　　　　　elasticsearch可以对日期格式化为字符串存储，但是建议存储为毫秒值，存储为long，节省空间。
     * 　　index：是否索引，布尔类型，默认是true
     * 　　store：是否存储，布尔类型，默认是false
     * 　　analyzer：分词器名称
     */

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String title;

    /**摘要*/
    private String abstracts;

    @Field(type = FieldType.Text)
    private String content;

    /**发表时间*/
    @Field(type= FieldType.Long)
    @JSONField(format = "yyyy-mm-dd hh:mm:ss")
    private Date postTime;

    /**点击率*/
    @Field(type = FieldType.Integer)
    private Long clickCount;


}
