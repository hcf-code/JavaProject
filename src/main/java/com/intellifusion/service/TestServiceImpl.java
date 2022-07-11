package com.intellifusion.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.intellifusion.entity.*;
import com.intellifusion.mapper.TestMapper;
import com.intellifusion.repository.TestRepository;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.ml.job.results.Bucket;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.join.JoinField;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, TestEntity> implements TestService {

    @Autowired
    TestMapper testMapper;


    @Autowired
    ElasticsearchRestTemplate restTemplate;

    @Autowired
    TestRepository testRepository;

    @Autowired
    ElasticsearchOperations elasticsearchOperations;




    @Override
    public TestEntity TestHcf(Long id) {
        return testRepository.findById(id).orElse(new TestEntity());
    }

    @Override
    public void bulkInsert() throws CloneNotSupportedException {
        List<String> list = new ArrayList<>();
        String sql = "insert into test (name,age,sex,sale) values ";
        for (int i = 0; i < 10000; i++) {
            RandInfo randInfo = new RandInfo();
            // 姓氏随机生成
            String familyName = randInfo.randFamilyName();
            // 名字依托于性别产生
            String randName = randInfo.randName(randInfo.randSex());
            String[] fixed = randName.split("-");
            String name = fixed[0];
            String sex = fixed[1];
            int age = randInfo.randAge();
            sql += "('" + familyName.concat(name) + "'," + age + ",'" + sex + "'," + randInfo.randSale() + "),";
        }
        testMapper.insertMapper(sql.substring(0, sql.length() - 1));
    }


    @Override
    public void bulkSaveES() {
        for (int i = 0; i < 101; i++) {
            QueryWrapper<TestEntity> wrapper = new QueryWrapper<TestEntity>().gt("id", 100000 * i).le("id", 100000 * (i + 1));
            List<TestEntity> testEntities = testMapper.selectList(wrapper);
            for (TestEntity entity : testEntities)
                entity.setFullName(entity.getName());
            restTemplate.save(testEntities);
        }
    }


    @Override
    public List<TestEntity> SearchES(String name, Double sale) {
        long start = System.currentTimeMillis();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("fullName", name));
        if (sale != -1)
            boolQueryBuilder.must(QueryBuilders.termQuery("sale", sale));
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(PageRequest.of(0, 10)).build();
        SearchHits<TestEntity> search = restTemplate.search(build, TestEntity.class);
        List<SearchHit<TestEntity>> searchHits = search.getSearchHits();
        System.out.println(System.currentTimeMillis() - start + "ms");
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }


    @Override
    public void reIndex() {
        IndexOperations indexOperations = restTemplate.indexOps(TestEntity.class);
        indexOperations.delete();
    }

    @Override
    public List<HashMap<String, Long>> aggSelect(Integer group, String name) {
        List<HashMap<String, Long>> list = new ArrayList<>();
        long start = System.currentTimeMillis();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.termQuery("fullName", name));
        NativeSearchQueryBuilder aggregation = new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.terms("name").field("name").size(group));
        NativeSearchQuery build = aggregation
                .withPageable(PageRequest.of(0, 1))
                .withQuery(boolQueryBuilder)
                .build();
        MultiBucketsAggregation bucketsAggregation = restTemplate.search(build, TestEntity.class).getAggregations().get("name");
        for (MultiBucketsAggregation.Bucket bucket : bucketsAggregation.getBuckets()) {
            HashMap<String, Long> map = new HashMap<>();
            map.put(bucket.getKeyAsString(), bucket.getDocCount());
            list.add(map);
        }
        System.out.println(System.currentTimeMillis() - start + " ms");
        return list;
    }


    public List<PersonRecordEntityES> selectPersonReword(){
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.filter(QueryBuilders.termQuery("personName","峰"));
        boolQueryBuilder.filter(QueryBuilders.termQuery("deviceDirection",1));
        boolQueryBuilder.filter(QueryBuilders.termQuery("personType",1));
        boolQueryBuilder.filter(QueryBuilders.termQuery("orgId",807));
        boolQueryBuilder.should(QueryBuilders.termQuery("type",2));

        BoolQueryBuilder innerBuilder = new BoolQueryBuilder();
        innerBuilder.mustNot(QueryBuilders.existsQuery("type"));
        boolQueryBuilder.should(innerBuilder);

        RangeQueryBuilder rangeQuery = new RangeQueryBuilder("recordTime").gt(1621537940000L).lt(1634029140000L);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(PageRequest.of(0, 1))
                .withQuery(boolQueryBuilder)
                .withFilter(rangeQuery)
                .withSort(SortBuilders.fieldSort("recordTime").order(SortOrder.DESC))
                .build();
        SearchHits<PersonRecordEntityES> search = restTemplate.search(searchQuery, PersonRecordEntityES.class);

        return  search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    public static DiskInfo hcf() throws ExecutionException, InterruptedException {
        DiskInfo diskInfo;
        Thread.sleep(100);
        System.out.println("woaini");
        CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {
            System.out.println("子线程1");
            return "3000MB";
        });

        CompletableFuture<String> async2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("子线程2");
            return "hcf";
        });

        diskInfo = new DiskInfo("macOs","500GB","200GB","300GB","40%","wwww");


        String mount = async2.get();
        diskInfo.setMountedOn(mount);

        String total = async.get();
        diskInfo.setTotalSize(total);

        System.out.println("抓线程100");

        return diskInfo;
    }

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        for (int i = 0; i <100; i++) {
//            DiskInfo hcf = hcf();
//            System.out.println(hcf);
//        }
//
//    }

}
