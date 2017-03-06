package com.david.mahout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class ItemCFRun {

	private static final String USERS_DAT = "users.dat";
	private static final Logger log = Logger.getLogger(ItemCFRun.class);

	public static void main(String[] args) {
		// doRecommender(initUserBasedRecommender());
		doUserRecommender();
	}

	/**
	 * 单机情况使用Mahout做基于用户的推荐
	 */
	private static void doUserRecommender() {
		Recommender recommender = null;
		try {
			// 使用的用户数据模型
			DataModel dataModel = new FileDataModel(new File(readOfFilepath(null, USERS_DAT)), "::");
			// 使用皮尔逊相似度算法计算用户相似度（三元组情况下使用）
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
			// 对于有些项目或者我们自己都不是很清楚的推荐类（一元组情况下使用）
			// UserSimilarity userSimilarity = new
			// LogLikelihoodSimilarity(dataModel); //
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(4, userSimilarity, dataModel);
			recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);

			// 给用户1推荐10个物品
			List<RecommendedItem> recommendations = recommender.recommend(1, 10);
			for (RecommendedItem recommendation : recommendations) {
				System.out.println(recommendation);
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		} catch (TasteException tex) {
			log.error(tex.getMessage(), tex);
		}
	}

	/**
	 * 初始化基于用户的推荐器
	 */
	private static Recommender initUserBasedRecommender() {
		Recommender recommender = null;
		try {
			// 使用的用户数据模型
			DataModel dataModel = new FileDataModel(new File(readOfFilepath(null, USERS_DAT)), "::");
			// 使用皮尔逊相似度算法计算用户相似度（三元组情况下使用）
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
			// 对于有些项目或者我们自己都不是很清楚的推荐类（一元组情况下使用）
			// UserSimilarity userSimilarity = new
			// LogLikelihoodSimilarity(dataModel); //
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(4, userSimilarity, dataModel);
			recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
			return recommender;
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		} catch (TasteException tex) {
			log.error(tex.getMessage(), tex);
		}
		return recommender;
	}

	/**
	 * 做推荐
	 * 
	 * @throws TasteException
	 */
	private static void doRecommender(Recommender recommender) throws TasteException {
		// 给用户1推荐10个物品
		List<RecommendedItem> recommendations = recommender.recommend(1, 4);

		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}
	}

	/**
	 * 读取文件路径
	 * 
	 * @param folder
	 * @param fileName
	 * @return
	 */
	private static String readOfFilepath(String folder, String fileName) {
		String filepath = MahoutRun.class.getClassLoader().getResource("").getPath() + File.separator + "datasets";
		if (StringUtils.isNotBlank(folder)) {
			filepath += (File.separator + folder);
		}

		filepath += (File.separator + fileName);
		return filepath;
	}
}
