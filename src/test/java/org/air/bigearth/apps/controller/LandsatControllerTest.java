package org.air.bigearth.apps.controller;

import org.air.bigearth.apps.util.DataKeyUtil;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LandsatControllerTest {
    @Autowired
    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。
    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext
    //@Autowired
    private MockHttpSession session;// 注入模拟的http session
    //@Autowired
    private MockHttpServletRequest request; // 注入模拟的http request

    private MockHttpServletResponse response;

    /**
     * 在测试开始前初始化工作
     */
    @Before
    public void setUp() throws Exception{
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
    }

    /**
     * 数据查询接口
     *
     * @throws Exception
     */
    @Test
    public void testQuery2() throws Exception{
        this.mockMvc.perform(post("/retrieve/query2")
                .param("index", "landsat8")
                .param("name", "row")
                .param("key", "31"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 元数据查询接口
     *
     * @throws Exception
     */
    @Test
    public void testMtl() throws Exception {
        this.mockMvc.perform(get("/retrieve/mtl")
                    .param("index", "lc8")
                    .param("type", "mtl")
                    .param("id", "LC81220332015067LGN00"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testInsert() throws Exception {
        this.mockMvc.perform(get("/retrieve/insert")
                .param("index", "data")
                .param("type", "json")
                .param("id", "1")
                .param("json", "{\"extractType\":\"dsafa\",\"filePath\":\"123\",\"location\":\"154\",\"uUID\":\"sd123\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    public void testGetDataset() throws Exception {
        this.mockMvc.perform(get("/retrieve/getDataset"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetDatasetWithPage() throws Exception{
        mockMvc.perform(get("/retrieve/getDatasetWithPage")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testSessionPage() throws Exception{
        int offset = 50;
        int pageSize = 10;
        int size = 163;
        // 起始条数
        int startNum = offset;
        // 当前页
        int currentPage = offset / pageSize + 1;
        // 终止条数
        int endNum = size / pageSize >= currentPage ? (currentPage * pageSize) - 1 : size - 1;
        for (int i = startNum; i <= endNum; i++) {
            System.out.println(i);
        }
    }

    @After
    public void tearDown() throws Exception {

    }

    public static void main(String[] args) {
        System.out.println(DataKeyUtil.getDataKey());
    }
}
