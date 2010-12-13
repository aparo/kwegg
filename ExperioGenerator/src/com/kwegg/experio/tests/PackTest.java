package com.kwegg.experio.tests;

import java.io.IOException;

import org.json.JSONException;

import com.kwegg.commons.experio.BaseExperio;
import com.kwegg.commons.news.CloudNews;
import com.kwegg.commons.pack.CoreBareExperioPack;
import com.kwegg.extractor.BaseExperioExtractor;
import com.kwegg.models.NewsTableHandler;
/**
 * this method is deprecated
 * @author parag
 *
 */
@Deprecated
public class PackTest {
	
	public void getNewsExperios() throws JSONException {
		CloudNews cn = NewsTableHandler.getInstance().getNewsById(10);
		String content = cn.getContent();
		
		try {
			BaseExperio[] experios = BaseExperioExtractor.getInstance().extractExperios(content);
			CoreBareExperioPack pack = new CoreBareExperioPack(cn);
			pack.createPack(experios);
			System.out.println(pack.toPublish());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws JSONException {
		PackTest pt = new PackTest();
		pt.getNewsExperios();
	}

}
