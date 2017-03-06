package com.david.mahout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MahoutRun {
	private static final String MOVIES_DAT = "movies.dat";
	private static final String USERS_DAT = "users.dat";
	private static final String RATINGS_DAT = "ratings.dat";

	public static void main(String[] args) throws TasteException {
		// System.out.println(readOfFilepath(null, MOVIES_DAT));
		runUserDatasetsRecommend();
	}

	public static void runUserDatasetsRecommend() throws TasteException {
		try {
			DataModel dataModel = new FileDataModel(new File(readOfFilepath(null, USERS_DAT)), "::");
			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
			double userSimilarity1And18 = userSimilarity.userSimilarity(1, 18);
			System.out.println(userSimilarity1And18);
			
			//选择邻居用户，使用NearestNUserNeighborhood实现UserNeighborhood接口，选择邻近的4个用户
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(4, userSimilarity, dataModel);
			Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);

			//给用户1推荐4个物品
			List<RecommendedItem> recommendations = recommender.recommend(1, 4);

			for (RecommendedItem recommendation : recommendations) {
			    System.out.println(recommendation);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String readOfFilepath(String folder, String fileName) {
		String filepath = MahoutRun.class.getClassLoader().getResource("").getPath() + File.separator + "datasets";
		if (StringUtils.isNotBlank(folder)) {
			filepath += (File.separator + folder);
		}

		filepath += (File.separator + fileName);
		return filepath;
	}
}
