package com.nikesh.as.imagesearch.ImageSearch;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.imagesearch.model.v20180120.*;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.io.*;
import java.util.List;

public class ImageSearchDemo {
    private String accessKeyId;
    private String accessKeySecret;
    private String instanceName;
    private String regionId;

    private IAcsClient client;

    public ImageSearchDemo(String accessKeyId, String accessKeySecret, String instanceName, String regionId) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.instanceName = instanceName;
        this.regionId = regionId;
    }

    public boolean initAcsClient() {
        // set client end time-out
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // add user-defined endpoint
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        try {
            if (regionId.compareToIgnoreCase("cn-shanghai") == 0) {
                DefaultProfile.addEndpoint(regionId, regionId,
                        "ImageSearch", "imagesearch.cn-shanghai.aliyuncs.com");
            } else if (regionId.compareToIgnoreCase("ap-southeast-2") == 0) {
                DefaultProfile.addEndpoint(regionId, regionId,
                        "ImageSearch", "imagesearch.ap-southeast-2.aliyuncs.com");
            } else {
                System.out.println("invalid regionId " + regionId);
                return false;
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }
        client = new DefaultAcsClient(profile);

        return true;
    }

    private void testAddItem() {
        AddItemRequest request = new AddItemRequest();
        request.setInstanceName(instanceName);
        request.setCatId("0");
        request.setItemId("100000");
        //request.setCustContent("{\"key words\":\"fashion, beautiful\"}");
        request.setCustContent("{\"key words\":\"Dress, girl_tshirt\"}");
        /*byte[] bytes1 = getBytes("/Users/nikeshgogia/Documents/workspace/ImageSearch/src/main/java/resources/girl_cloth3.jpg");
        if (bytes1.length == 0) {
            System.out.println("read picture content failed.");
            return;
        }
        String picName1 = "girl_cloth3";
        request.addPicture(picName1, bytes1);

        byte[] bytes2 = getBytes("/Users/nikeshgogia/Documents/workspace/ImageSearch/src/main/java/resources/girl_cloth4.jpg");
        if (bytes2.length == 0) {
            System.out.println("read picture content failed.");
            return;
        }
        String picName2 = "girl_cloth4";
        request.addPicture(picName2, bytes2);
        */
        byte[] bytes3 = getBytes("/Users/nikeshgogia/Documents/workspace/ImageSearch/src/main/java/resources/clothes.jpg");
        if (bytes3.length == 0) {
            System.out.println("read picture content failed.");
            return;
        }
        String picName3 = "jacket";
        request.addPicture(picName3, bytes3);

        if (!request.buildPostContent()) {
            System.out.println("build post content failed.");
            return;
        }
        
        else {
        	System.out.println("Image Added");
        }

        AddItemResponse response = null;
        try {
            response = client.getAcsResponse(request);
            System.out.println(response.getSuccess());
            System.out.println(response.getRequestId());
            System.out.println(response.getCode());
            System.out.println(response.getMessage());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void testSearchItem() {
        SearchItemRequest request = new SearchItemRequest();
        request.setInstanceName(instanceName);
        request.setCatId("0");
        request.setStart(0);
        request.setNum(10);

        //byte[] bytes = getBytes("/Users/nikeshgogia/Documents/workspace/ImageSearch/src/main/java/resources/girl_cloth3.jpg");
        byte[] bytes = getBytes("/Users/nikeshgogia/Documents/workspace/ImageSearch/src/main/java/resources/flow.jpg");
        if (bytes.length == 0) {
            System.out.println("read picture content failed.");
            return;
        }
        request.setSearchPicture(bytes);

        if (!request.buildPostContent()) {
            System.out.println("build post content failed.");
            return;
        }

        SearchItemResponse response = null;
        try {
            response = client.getAcsResponse(request);
            
            System.out.println(response.getSuccess());
            System.out.println(response.getRequestId());
            System.out.println(response.getCode());
            System.out.println(response.getMessage());
            System.out.println("Inside Search-->");
            
            SearchItemResponse.Head head = response.getHead();
            SearchItemResponse.PicInfo picInfo = response.getPicInfo();
            List<SearchItemResponse.Auction> auctions = response.getAuctions();
            System.out.println("--------- Head ------------");
            System.out.println(head.getDocsFound());
            System.out.println(head.getDocsReturn());
            System.out.println(head.getSearchTime());

            System.out.println("--------- PicInfo ------------");
            System.out.println(picInfo.getRegion());
            System.out.println(picInfo.getCategory());
            for (SearchItemResponse.PicInfo.Category category : picInfo.getAllCategory()) {
                System.out.println("   ------ Category ---------");
                System.out.println(category.getId());
                System.out.println(category.getName());
            }

            System.out.println("--------- Auctions ------------");
            for (SearchItemResponse.Auction auction : auctions) {
                System.out.println("   ------ Auction ---------");
                System.out.println(auction.getItemId());
                System.out.println(auction.getPicName());
                System.out.println(auction.getCatId());
                System.out.println(auction.getCustContent());
                System.out.println(auction.getSortExprValues());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private void testDeleteItem() {
        DeleteItemRequest request = new DeleteItemRequest();
        request.setInstanceName(instanceName);
        request.setItemId("100000");
        // request.addPicture("girl_cloth4.jpg");
        if (!request.buildPostContent()) {
            System.out.println("build post content failed.");
            return;
        }

        DeleteItemResponse response = null;
        try {
            response = client.getAcsResponse(request);
            System.out.println(response.getSuccess());
            System.out.println(response.getRequestId());
            System.out.println(response.getCode());
            System.out.println(response.getMessage());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(2000 * 1024); // picture max size is 2MB
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void main(String[] args) throws InterruptedException {
        // user need specifiy

        
        String accessKeyId = "";
        String accessKeySecret = "";
        
        String instanceName = "nikesh";

        if (accessKeyId.length() == 0 || accessKeySecret.length() == 0 || instanceName.length() == 0) {
            System.out.println("accessKeyId, accessKeySecret and instanceName must specify.");
            return;
        }

        //String regionId = "cn-shanghai"; // ap-southeast-1 // ap-southeast-2
        String regionId = "ap-southeast-2";
        ImageSearchDemo demo = new ImageSearchDemo(accessKeyId, accessKeySecret, instanceName, regionId);
        boolean result = demo.initAcsClient();
        if (!result) {
            System.out.println("init acs client failed.");
            return;
        }
        demo.testAddItem();

        //Thread.sleep(5000);

        demo.testSearchItem();

        //Thread.sleep(1000);

        //demo.testDeleteItem();

        //Thread.sleep(5000);

        //demo.testSearchItem();
    }

}
