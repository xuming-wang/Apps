package org.air.bigearth.apps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppsApplicationTests {

	@Test
	public void contextLoads() {
		String s = "/home/zds/Documents/mywork/data/TiffPic/LC08/tiffoutputpath/test20.TIFF";
		System.out.println(s.substring(0, s.lastIndexOf("/")));
	}



}
