package com.david.mahout;

import org.apache.hadoop.hdfs.server.namenode.status_jsp;
import org.apache.hadoop.mapred.JobConf;
import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.hadoop.item.RecommenderJob;

public class DistributedDemo {
	private static final String HDFS = "hdfs://192.168.1.210:9000";
	private static final Logger log = Logger.getLogger(DistributedDemo.class);

	public static void main(String[] args) {
		run(args);
	}

	public static void run(String[] args) {
		try {
			String localFile = "filedata/item.csv";
			String inPath = HDFS + "/users/hdfs/userCF";
			String inFile = inPath + "/item.csv";
			String outPath = HDFS + "/users/hdfs/userCF/result/";
			String outFile = outPath + "/part-r-0000";
			String tmpPath = HDFS + "/temp/" + System.currentTimeMillis();

			JobConf conf = config();
			HdfsDAO hdfs = new HdfsDAO(HDFS, conf);
			hdfs.rmr(inPath);
			hdfs.mkdirs(inPath);
			hdfs.copyFile(localFile, inPath);
			hdfs.ls(inPath);
			hdfs.cat(inFile);
			
			StringBuilder sb = new StringBuilder();
			sb.append("--input ").append(inPath);
			sb.append(" --output ").append(outPath);
			sb.append(" --booleanData true");
			sb.append(" --similarityClassname org.apache.mahout.math.hadoop.similarity."
					+ "cooccurrence.measures.PearsonCorrelationSimilarity");
			sb.append(" --tempDir ").append(tmpPath);
			args = sb.toString().split(" ");

			RecommenderJob job = new RecommenderJob(); //封装了分步式并行算法的执行过程
			job.setConf(conf);
			job.run(args);
			hdfs.cat(outFile);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	public static JobConf config() {
		JobConf conf = new JobConf(ItemCFHadoop.class);
		conf.setJobName("ItemCFHadoop");
		conf.addResource("classpath:/hadoop/sys_core_site.xml");
		conf.addResource("classpath:/hadoop/sys_hdfs_site.xml");
		conf.addResource("classpath:/hadoop/sys_mapreduce_site.xml");
		return conf;
	}
}
