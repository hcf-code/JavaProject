package com.intellifusion.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.intellifusion.entity.PersonRecordEntity;

import com.intellifusion.entity.PersonRecordEntityES;
import com.intellifusion.entity.RandInfo;
import com.intellifusion.mapper.PostgreMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class MongoDbServiceImpl extends ServiceImpl<PostgreMapper, PersonRecordEntityES> implements MongoDbService {


    private static final ThreadPoolExecutor threadPoolExecutor;

    private static final AtomicLong shareId = new AtomicLong(1);


    static {
        threadPoolExecutor = new ThreadPoolExecutor(100, 205, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void selectOne() {
        List<PersonRecordEntity> recordEntity = mongoTemplate.findAll(PersonRecordEntity.class);
        System.out.println(recordEntity);
    }


    //批量插入
    @Override
    public synchronized void bulkInsertData() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(threadPoolExecutor.getCorePoolSize());
        for (int i = 0; i < threadPoolExecutor.getCorePoolSize(); i++) {
            Runnable runnable = () -> insertListToMongo(Thread.currentThread(), countDownLatch);
            threadPoolExecutor.execute(runnable);
        }
        countDownLatch.await();
        System.out.println("总任务执行结束!");
    }

    public void insertListToMongo(Thread thread, CountDownLatch countDownLatch) {
        long start = System.currentTimeMillis();
        System.out.println("线程名称： " + thread.getName() + " 已开始执行！ ");
        //每个线程承担 100*10000=100万
        for (int i = 0; i < 100; i++) {
            //临时存储10000个对象
            List<PersonRecordEntityES> list = new ArrayList<>();
            List<PersonRecordEntityES> entityList = CreateTenThousand(list);
            //elasticsearchRestTemplate.save(entityList);
            //mongoTemplate.insertAll(Objects.requireNonNull(entityList));
            saveBatch(entityList);
        }
        System.out.println("线程名称： " + thread.getName() + " 执行结束！执行时间：" + ((System.currentTimeMillis() - start) / 1000) + " s");
        countDownLatch.countDown();
    }

    private List<PersonRecordEntityES> CreateTenThousand(List<PersonRecordEntityES> list) {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            RandInfo randInfo = new RandInfo();
            String randName = randInfo.randName(randInfo.randSex());
            String familyName = randInfo.randFamilyName();
            String[] fixed = randName.split("-");
            String name = fixed[0];

            PersonRecordEntityES recordEntity = new PersonRecordEntityES();
            recordEntity.setId(shareId.getAndIncrement());
            recordEntity.setPersonRegImageUrl("http://192.168.12.143:30089/group1/M00/00/03/EfQABmFVWsOAHWz5AALO1f2rgws357.jpg");
            recordEntity.setPersonRecordImageUrl("http://192.168.12.143:30089/group1/M00/00/04/EfQABmFVoHWACmALAAO7QulcSXc962.jpg");
            recordEntity.setPersonFaceImageUrl("http://hcf_test");
            recordEntity.setPersonName(familyName.concat(name));
            recordEntity.setConfidence(randInfo.randDouble());
            recordEntity.setPersonNum("访客");
            recordEntity.setOrgId(randInfo.randOrgId());
            recordEntity.setPersonId("1516546163131554646");
            //recordEntity.setRecordTime(new Date(randomDate()));
            recordEntity.setRecordTime(randomDate());
            recordEntity.setDeviceType("人脸识别门禁机");
            recordEntity.setPersonType(random.nextInt(4));
            recordEntity.setDeviceTypeId(0);
            recordEntity.setDeviceId("HTBBA51A00000" + random.nextInt(10));
            recordEntity.setDeviceName("测试设备00" + random.nextInt(10));
            recordEntity.setDeviceAddress(random.nextInt(33) + 1 + "楼");
            recordEntity.setDeviceDirection(random.nextInt(4) - 1);
            recordEntity.setRecgTime(randomDate());
            recordEntity.setType(random.nextInt(6));
            recordEntity.setTemperature((float) (random.nextInt(10) + 30));
            recordEntity.setInterviwee("hcf 家的" + name);
            recordEntity.setInterviweePhone("13155862737");
            //recordEntity.setSplitName(splitName(recordEntity.getPersonName().toCharArray(),"",0,0));
            list.add(recordEntity);
        }
        return list;
    }

    //随机生成时间戳
    public long randomDate() {
        long start = Timestamp.valueOf("2020-01-01 00:00:00").getTime();
        long end = Timestamp.valueOf("2021-12-20 00:00:00").getTime();
        return (long) ((end - start) * Math.random() + start);
    }

    //0  //0

    /**
     * 用于姓名分词,首尾指针开始指向一处也就是第一个元素的索引位置，后面进行指针移动,splitNameStr默认传空值
     * @param source
     * @param tmp
     * @param head
      * @param tail
     * @return
     */
    public String separate(char[] source, String tmp, int head, int tail) {
        if (head == source.length-1 && tail == source.length)
            return tmp;
        if (tail == source.length)
            tmp = separate(source, tmp, head + 1, head + 1);
        else {
            tmp += new String(Arrays.copyOfRange(source, head, tail + 1)) + " ";
            tmp = separate(source, tmp, head, tail+1);
        }
        return tmp;
    }

    public static void main(String[] args) throws Exception {
        String hcf = "韩长峰";
        System.out.println();
    }

        //Segment segment = HanLP.newSegment().enableNameRecognize(true);
        //System.out.println(segment.seg("韩长峰"));
        //SpeedTokenizer.segment("韩长峰");
        //List<Term> termList = IndexTokenizer.segment("韩长峰");
        //List<String> keywordList = HanLP.extractKeyword("韩长峰", 100);

        //char[] chars = new char[]{'s','f','g'};
//        System.out.println();
//        char[] copy = Arrays.copyOfRange(chars, 0, 1);
//        System.out.println(chars);
//        String s = new String(chars);
//        System.out.println(s);

//        String text = "韩长峰";
//        IKAnalyzer analyzer = new IKAnalyzer(true);
//        StringReader reader = new StringReader(text);
//        TokenStream tokenStream = analyzer.tokenStream("", reader);
//        tokenStream.reset();
//        CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
//        while (tokenStream.incrementToken()){
//            System.out.println(term.toString());
//        }
//        reader.close();



    public static int sum(int n) {
        int sum;
        int carryNum = 0;
        int count = 0;
        while (n > 0) {
            sum = n + carryNum % 10;
            if (1 == sum % 10)
                count++;
            carryNum /= 10;
            carryNum += sum / 10;
            n--;
        }
        return count;
    }

}
