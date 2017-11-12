package com.taotao.test.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.web.util.FastDFSClient;

public class FastDFSTest {
	//上传文件
	@Test
	public void testUpload() throws Exception{
		//1.创建连接到fastdfs服务器配置文件
		
		//2.加载全局的配置文件
		ClientGlobal.init("C:/Users/ThinkPad/workspace-taotao-12/taotao-manager-web/src/main/resources/properties/fast_dfs.conf");
		//3.创建trackerClient对象  为了 获取到trackerserver
		TrackerClient trackerClient = new TrackerClient();
		//4.创建trackerserver对象  通过trackerClient对象获取的
		TrackerServer trackerServer = trackerClient.getConnection();
		//5.指定一个引用 storageServer 为null就可以了
		StorageServer storageServer = null;
		//6.创建storageClient对象    对象提供了上传文件 和下载文件的方法
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		
		//7.上传文件
		//参数1：文件的路径
		//参数2：文件的扩展名 不带点
		//参数3：元数据
		String[] strings = storageClient.upload_file("G:/temp/3e119319-11eb-4b28-a21c-e52a17a59b40.jpg", "jpg", null);
		
		for (String string : strings) {
			System.out.println(string);
		}
		
	}
	
	@Test
	public void testFASTDFSClient() throws Exception{
		FastDFSClient dfsClient = new FastDFSClient("C:/Users/ThinkPad/workspace-taotao-12/taotao-manager-web/src/main/resources/properties/fast_dfs.conf");
		String string = dfsClient.uploadFile("C:/Users/ThinkPad/Pictures/1e17d3ca7dffd144d3b41a991d81cfbe_b.jpg", "jpg");
		System.out.println(string);
	}
}
