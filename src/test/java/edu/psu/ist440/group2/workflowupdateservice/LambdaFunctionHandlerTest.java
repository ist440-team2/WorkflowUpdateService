package edu.psu.ist440.group2.workflowupdateservice;

import static org.junit.Assert.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import edu.psu.ist440.group2.workflowupdateservice.JobItem.DecryptedInfo;
import edu.psu.ist440.group2.workflowupdateservice.JobItem.OCRInfo;
import edu.psu.ist440.group2.workflowupdateservice.JobItem.UploadedImageInfo;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LambdaFunctionHandlerTest {

    private JobItem input;
    private static final String EXPECTED_JOBID = UUID.randomUUID().toString();
    private static final String EXPECTED_USERID = "jen";
    private static final String EXPECTED_CREATED_DATE = ZonedDateTime.now(ZoneId.of("UTC")).toString();
    private static final String EXPECTED_UPLOADED_INFO_BUCKET = "test_upload_bucket";
    private static final String EXPECTED_UPLOADED_INFO_KEY = "test_upload.txt";
    private static final String EXPECTED_OCRINFO_BUCKET = "ist440grp2ocr";
    private static final String EXPECTED_OCRINFO_KEY = "jen_zodiacTest1.txt";
    private static final String EXPECTED_DECRYPTED_METHOD_1 = "method1";
    private static final String EXPECTED_DECRYPTED_BUCKET_1 = "decrypted_bucke_1t";
    private static final String EXPECTED_DECRYPTED_KEY_1 = "decrypted_key_1";
    private static final String EXPECTED_DECRYPTED_METHOD_2 = "method2";
    private static final String EXPECTED_DECRYPTED_BUCKET_2 = "decrypted_bucket_2";
    private static final String EXPECTED_DECRYPTED_KEY_2 = "decrypted_key_2";
    private static final String EXPECTED_TRANSLATED_BUCKET = "transleted_bucket";
    private static final String EXPECTED_TRANSLATED_KEY = "transleted_key";
    private static final String EXPECTED_STATUS = "QUEUED";
    
    
    @Before
    public void setUp() throws Exception {
    	
    	UploadedImageInfo uii = new UploadedImageInfo();
    	uii.setBucket(EXPECTED_UPLOADED_INFO_BUCKET);
    	uii.setKey(EXPECTED_UPLOADED_INFO_KEY);
    	
    	OCRInfo ocrInfo = new OCRInfo();
    	ocrInfo.setBucket(EXPECTED_OCRINFO_BUCKET);
    	ocrInfo.setKey(EXPECTED_OCRINFO_KEY);
    	
    	DecryptedInfo decryptedInfo1 = new DecryptedInfo();
    	decryptedInfo1.setMethod(EXPECTED_DECRYPTED_METHOD_1);
    	decryptedInfo1.setDecryptedBucket(EXPECTED_DECRYPTED_BUCKET_1);
    	decryptedInfo1.setDecryptedKey(EXPECTED_DECRYPTED_KEY_1);
    	decryptedInfo1.setTranslatedBucket("bucket1");
    	decryptedInfo1.setTranslatedKey("key1");
    	
    	DecryptedInfo decryptedInfo2 = new DecryptedInfo();
    	decryptedInfo2.setMethod(EXPECTED_DECRYPTED_BUCKET_2);
    	decryptedInfo2.setDecryptedBucket(EXPECTED_DECRYPTED_BUCKET_2);
    	decryptedInfo2.setDecryptedKey(EXPECTED_DECRYPTED_KEY_2);
    	decryptedInfo2.setTranslatedBucket("bucket2");
    	decryptedInfo2.setTranslatedKey("key2");
    	
    	ArrayList<DecryptedInfo> decrypted = new ArrayList<>();
    	decrypted.add(decryptedInfo1);
    	decrypted.add(decryptedInfo2);
    	
    	input = new JobItem();
        input.setCreatedDate(EXPECTED_CREATED_DATE);
        input.setJobId(EXPECTED_JOBID);
        input.setUserId(EXPECTED_USERID);
        input.setStatus(EXPECTED_STATUS);
        input.setUploadedImageInfo(uii);
        input.setOcrInfo(ocrInfo);
        input.setDecryptedInfo(decrypted);
    }


    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        Context ctx = createContext();

        JobItem result = handler.handleRequest(input, ctx);
        assertEquals(EXPECTED_USERID, result.getUserId());
        assertEquals(EXPECTED_JOBID, result.getJobId());
        assertEquals(EXPECTED_CREATED_DATE, result.getCreatedDate());
        assertEquals(EXPECTED_STATUS, result.getStatus());

    }
}
