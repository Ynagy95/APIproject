package proj.system;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import proj.utils.datareader.PropertyReader;
import proj.utils.report.AllureRestAssured;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.internal.matchers.StringContains.containsString;

public class E2ETests {

    @BeforeClass
    public void setup(){
        RestAssured.baseURI = PropertyReader.getProperty("baseUrl");
        RestAssured.filters(new ResponseTimeFilter());


    }

    @Test(priority = 1)
    public void createListbeforeboard() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "my list")
                        .queryParam("idBoard",Config.boardId)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("color", "green"),
                "POST",
                "1/lists"
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid value for idBoard"));
        ResponseValidator.assertNotJson(response);

    }

    @Test(priority = 2)
    public void createboardwithoutname() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("desc", "we are making boards for our Iti project"),
                "POST",       // HTTP method
                "1/boards"     // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                // check exact values
                .body("message", equalTo("invalid value for name"))
                .body("error", equalTo("ERROR"))
                // check types
                .body("message", instanceOf(String.class))
                .body("error", instanceOf(String.class))
                .body("message", notNullValue())
                .body("error", notNullValue());
        ResponseValidator.assertJson(response);
    }

    @Test(priority = 3)
    public void createboard() {


        Response boardresponse = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "ITI")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("desc", "we are making boards for our Iti project"),
                "POST",
                "1/boards"
        );
        boardresponse.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("ITI"))
                .body("desc", equalTo("we are making boards for our Iti project"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("descData"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idOrganization"))
                .body("$", hasKey("idEnterprise"))
                .body("$", hasKey("pinned"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("$", hasKey("prefs"))
                .body("$", hasKey("labelNames"))
                .body("$", hasKey("limits"))

                // check types
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("desc", notNullValue())
                .body("descData", anyOf(nullValue(), instanceOf(String.class)))
                .body("closed", instanceOf(Boolean.class))
                .body("idOrganization", notNullValue())
                .body("idEnterprise", anyOf(nullValue(), instanceOf(String.class)))
                .body("pinned", instanceOf(Boolean.class))
                .body("url", notNullValue())
                .body("shortUrl", notNullValue())
                .body("prefs", instanceOf(Map.class))
                .body("labelNames", instanceOf(Map.class))
                .body("limits", instanceOf(Map.class))
                .extract().response().asString();
        JsonPath js = boardresponse.jsonPath();
        Config.boardId = js.getString("id");



        ResponseValidator.assertJson(boardresponse);
    }

    @Test(priority = 4)
    public void getboard() {



        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "GET",
                "1/boards/"+Config.boardId
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("ITI"))
                .body("desc", equalTo("we are making boards for our Iti project"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("descData"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idOrganization"))
                .body("$", hasKey("idEnterprise"))
                .body("$", hasKey("pinned"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("$", hasKey("prefs"))
                .body("$", hasKey("labelNames"))


                // check types
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("desc", notNullValue())
                .body("descData", anyOf(nullValue(), instanceOf(String.class)))
                .body("closed", instanceOf(Boolean.class))
                .body("idOrganization", notNullValue())
                .body("idEnterprise", anyOf(nullValue(), instanceOf(String.class)))
                .body("pinned", instanceOf(Boolean.class))
                .body("url", notNullValue())
                .body("shortUrl", notNullValue())
                .body("prefs", instanceOf(Map.class))
                .body("labelNames", instanceOf(Map.class));



        ResponseValidator.assertJson(response);
    }
    @Test(priority = 5)
    public void updateboard() {



        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "ITI1"),


                "PUT",
                "1/boards/"+Config.boardId
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("ITI1"))
                .body("desc", equalTo("we are making boards for our Iti project"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("descData"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idOrganization"))
                .body("$", hasKey("idEnterprise"))
                .body("$", hasKey("pinned"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("$", hasKey("prefs"))
                .body("$", hasKey("labelNames"))


                // check types
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("desc", notNullValue())
                .body("descData", anyOf(nullValue(), instanceOf(String.class)))
                .body("closed", instanceOf(Boolean.class))
                .body("idOrganization", notNullValue())
                .body("idEnterprise", anyOf(nullValue(), instanceOf(String.class)))
                .body("pinned", instanceOf(Boolean.class))
                .body("url", notNullValue())
                .body("shortUrl", notNullValue())
                .body("prefs", instanceOf(Map.class))
                .body("labelNames", instanceOf(Map.class));

        ResponseValidator.assertJson(response);
    }
    @Test(priority = 6)
    public void getboardafterupdate() {



        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/boards/"+Config.boardId
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("ITI1"))
                .body("desc", equalTo("we are making boards for our Iti project"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("descData"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idOrganization"))
                .body("$", hasKey("idEnterprise"))
                .body("$", hasKey("pinned"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("$", hasKey("prefs"))
                .body("$", hasKey("labelNames"))


                // check types
                .body("id", notNullValue())
                .body("name", notNullValue())
                .body("desc", notNullValue())
                .body("descData", anyOf(nullValue(), instanceOf(String.class)))
                .body("closed", instanceOf(Boolean.class))
                .body("idOrganization", notNullValue())
                .body("idEnterprise", anyOf(nullValue(), instanceOf(String.class)))
                .body("pinned", instanceOf(Boolean.class))
                .body("url", notNullValue())
                .body("shortUrl", notNullValue())
                .body("prefs", instanceOf(Map.class))
                .body("labelNames", instanceOf(Map.class));




        ResponseValidator.assertJson(response);
    }
    @Test(priority = 7)
    public void createcardbeforeList() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idList", Config.listid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "كارت")
                        .queryParam("desc", "يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق. تهدف هذه الميزة إلى تمكين المستخدم من إضافة قائمة جديدة تحت Board محدد، مع ضمان حفظها بنجاح في قاعدة البيانات وإمكانية استرجاعها من خلال واجهات الـ API المختلفة.\n" +
                                "تُعد خاصية إنشاء القوائم جزءاً أساسياً من بنية التطبيق، حيث تساعد المستخدم في تنظيم مهامه وتوزيع العمل داخل الـ Board. لذلك، فإن التأكد من أن عملية إنشاء القائمة تعمل بشكل صحيح يساهم في تحسين تجربة المستخدم ورفع جودة المنتج.\n" +
                                "\uD83C\uDFAF الهدف الأساسي:\n" +
                                "تمكين المستخدم من إنشاء قائمة جديدة مرتبطة بلوحة معينة بحيث يتم تخزينها بشكل صحيح ويمكن الحصول عليها أو تعديلها أو إدارتها لاحقاً.\n" +
                                "\uD83E\uDDE9 نطاق العمل (Scope):\n" +
                                "تنفيذ طلب POST مسؤول عن إنشاء القائمة الجديدة.\n" +
                                "التحقق من صحة البيانات المرسلة في جسم الطلب (مثل الاسم ومعرّف اللوحة).\n" +
                                "ضمان تخزين القائمة الجديدة في قاعدة البيانات بنجاح.\n" +
                                "التأكد من إمكانية استرجاع القائمة باستخدام طلب GET.\n" +
                                "حفظ الـ ID الناتج عن عملية الإنشاء لاستخدامه في اختبارات أخرى.\n" +
                                "التعامل مع الأخطاء الناتجة عن نقص أو خطأ في البيانات.\n" +
                                "إجراء اختبار يدوي وآلي للتأكد من عمل الخاصية من البداية للنهاية (E2E).\n" +
                                "\uD83D\uDCCC متطلبات الوظيفة (Functional Requirements):\n" +
                                "يجب أن يتمكّن المستخدم من إرسال طلب POST يحتوي على بيانات صحيحة لإنشاء القائمة.\n" +
                                "يجب أن يكون الـ Response يحتوي على كود الحالة 200 وبيانات القائمة الجديدة.\n" +
                                "يجب أن يحتوي الرد على معرّف فريد (ID) يتم توليده تلقائياً.\n" +
                                "يجب أن يكون اسم القائمة مطابقاً للاسم المُرسل في الطلب.\n" +
                                "يجب ربط القائمة بالـ Board الصحيح.\n" +
                                "يجب أن تظهر القائمة الجديدة عند تنفيذ طلب GET للقوائم.\n" +
                                "في حال وجود بيانات ناقصة أو خاطئة يجب إرجاع رسالة خطأ مناسبة وكود حالة صحيح (مثل 400)."),
                "POST",
                "1/cards"     // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid value for idList"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 8)
    public void createlistwithoutname() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "")
                        .queryParam("idBoard", Config.boardId)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("color", "green"),
                "POST",       // HTTP method
                "1/lists"     // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                // check exact values
                .body(containsString("invalid value for name"));
        ResponseValidator.assertNotJson(response);


    }
    @Test(priority = 9)
    public void createlistwithunknownparameter() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "my list")
                        .queryParam("idBoard", Config.boardId)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("color", "green")
                        .queryParam("width%26height", "600"),
                "POST",
                "1/lists"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("my list"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))
                .body("$", hasKey("limits"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("limits", instanceOf(Map.class))
                .body("idBoard", equalTo(expectedBoardId))
                .extract().response().asString();
        JsonPath js = response.jsonPath();
        Config.listid2 = js.getString("id");

        ResponseValidator.assertJson(response);
    }
    @Test(priority = 10)
    public void getlist() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "GET",
                "1/lists/"+Config.listid2
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("my list"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))


                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 11)
    public void createlist() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "my list")
                        .queryParam("idBoard", Config.boardId)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("color", "green"),
                "POST",
                "1/lists"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("my list"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))
                .body("$", hasKey("limits"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("limits", instanceOf(Map.class))
                .body("idBoard", equalTo(expectedBoardId))
                .extract().response().asString();
        JsonPath js = response.jsonPath();
        Config.listid = js.getString("id");

        ResponseValidator.assertJson(response);
    }
    @Test(priority = 12)
    public void getlist2() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "GET",
                "1/lists/"+Config.listid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("my list"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))


                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 13)
    public void updatelist() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "newlist"),

                "PUT",
                "1/lists/"+Config.listid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 14)
    public void getlistafterupdate() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "GET",
                "1/lists/"+Config.listid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 15)
    public void archivelist() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("value", "true"),


                "PUT",
                "1/lists/"+Config.listid+"/closed"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(true)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 16)
    public void getlistafterarchive() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/lists/"+Config.listid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(true)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }

    @Test(priority = 17)
    public void unarchivelist() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("value", "false"),


                "PUT",
                "1/lists/"+Config.listid+"/closed"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 18)
    public void getlistafterunarchive() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/lists/"+Config.listid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 19)
    public void unarchivelistagain() {
        String expectedBoardId = Config.boardId;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("value", "false"),


                "PUT",
                "1/lists/"+Config.listid+"/closed"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newlist"))
                .body("color", equalTo("green"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("color"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("type"))
                .body("$", hasKey("datasource"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", allOf(instanceOf(Boolean.class), equalTo(false)))
                .body("color", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("type", anyOf(nullValue(), instanceOf(String.class)))
                .body("datasource", instanceOf(Map.class))
                .body("datasource.filter", instanceOf(Boolean.class))
                .body("idBoard", equalTo(expectedBoardId));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 20)
    public void createcardwithtoomuchdesc() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idList", Config.listid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "كارت")
                        .queryParam("desc", "يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق. تهدف هذه الميزة إلى تمكين المستخدم من إضافة قائمة جديدة تحت Board محدد، مع ضمان حفظها بنجاح في قاعدة البيانات وإمكانية استرجاعها من خلال واجهات الـ API المختلفة.\n" +
                                "تُعد خاصية إنشاء القوائم جزءاً أساسياً من بنية التطبيق، حيث تساعد المستخدم في تنظيم مهامه وتوزيع العمل داخل الـ Board. لذلك، فإن التأكد من أن عملية إنشاء القائمة تعمل بشكل صحيح يساهم في تحسين تجربة المستخدم ورفع جودة المنتج.\n" +
                                "\uD83C\uDFAF الهدف الأساسي:\n" +
                                "تمكين المستخدم من إنشاء قائمة جديدة مرتبطة بلوحة معينة بحيث يتم تخزينها بشكل صحيح ويمكن الحصول عليها أو تعديلها أو إدارتها لاحقاً.\n" +
                                "\uD83E\uDDE9 نطاق العمل (Scope):\n" +
                                "تنفيذ طلب POST مسؤول عن إنشاء القائمة الجديدة.\n" +
                                "التحقق من صحة البيانات المرسلة في جسم الطلب (مثل الاسم ومعرّف اللوحة).\n" +
                                "ضمان تخزين القائمة الجديدة في قاعدة البيانات بنجاح.\n" +
                                "التأكد من إمكانية استرجاع القائمة باستخدام طلب GET.\n" +
                                "حفظ الـ ID الناتج عن عملية الإنشاء لاستخدامه في اختبارات أخرى.\n" +
                                "التعامل مع الأخطاء الناتجة عن نقص أو خطأ في البيانات.\n" +
                                "إجراء اختبار يدوي وآلي للتأكد من عمل الخاصية من البداية للنهاية (E2E).\n" +
                                "\uD83D\uDCCC متطلبات الوظيفة (Functional Requirements):\n" +
                                "يجب أن يتمكّن المستخدم من إرسال طلب POST يحتوي على بيانات صحيحة لإنشاء القائمة.\n" +
                                "يجب أن يكون الـ Response يحتوي على كود الحالة 200 وبيانات القائمة الجديدة.\n" +
                                "يجب أن يحتوي الرد على معرّف فريد (ID) يتم توليده تلقائياً.\n" +
                                "يجب أن يكون اسم القائمة مطابقاً للاسم المُرسل في الطلب.\n" +
                                "يجب ربط القائمة بالـ Board الصحيح.\n" +
                                "يجب أن تظهر القائمة الجديدة عند تنفيذ طلب GET للقوائم.\n" +
                                "في حال وجود بيانات ناقصة أو خاطئة يجب إرجاع رسالة خطأ مناسبة وكود حالة صحيح (مثل 400).يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق. تهدف هذه الميزة إلى تمكين المستخدم من إضافة قائمة جديدة تحت Board محدد، مع ضمان حفظها بنجاح في قاعدة البيانات وإمكانية استرجاعها من خلال واجهات الـ API المختلفة.\n" +
                                "تُعد خاصية إنشاء القوائم جزءاً أساسياً من بنية التطبيق، حيث تساعد المستخدم في تنظيم مهامه وتوزيع العمل داخل الـ Board. لذلك، فإن التأكد من أن عملية إنشاء القائمة تعمل بشكل صحيح يساهم في تحسين تجربة المستخدم ورفع جودة المنتج.\n" +
                                "\uD83C\uDFAF الهدف الأساسي:\n" +
                                "تمكين المستخدم من إنشاء قائمة جديدة مرتبطة بلوحة معينة بحيث يتم تخزينها بشكل صحيح ويمكن الحصول عليها أو تعديلها أو إدارتها لاحقاً.\n" +
                                "\uD83E\uDDE9 نطاق العمل (Scope):\n" +
                                "تنفيذ طلب POST مسؤول عن إنشاء القائمة الجديدة.\n" +
                                "التحقق من صحة البيانات المرسلة في جسم الطلب (مثل الاسم ومعرّف اللوحة).\n" +
                                "ضمان تخزين القائمة الجديدة في قاعدة البيانات بنجاح.\n" +
                                "التأكد من إمكانية استرجاع القائمة باستخدام طلب GET.\n" +
                                "حفظ الـ ID الناتج عن عملية الإنشاء لاستخدامه في اختبارات أخرى.\n" +
                                "التعامل مع الأخطاء الناتجة عن نقص أو خطأ في البيانات.\n" +
                                "إجراء اختبار يدوي وآلي للتأكد من عمل الخاصية من البداية للنهاية (E2E).\n" +
                                "\uD83D\uDCCC متطلبات الوظيفة (Functional Requirements):\n" +
                                "يجب أن يتمكّن المستخدم من إرسال طلب POST يحتوي على بيانات صحيحة لإنشاء القائمة.\n" +
                                "يجب أن يكون الـ Response يحتوي على كود الحالة 200 وبيانات القائمة الجديدة.\n" +
                                "يجب أن يحتوي الرد على معرّف فريد (ID) يتم توليده تلقائياً.\n" +
                                "يجب أن يكون اسم القائمة مطابقاً للاسم المُرسل في الطلب.\n" +
                                "يجب ربط القائمة بالـ Board الصحيح.\n" +
                                "يجب أن تظهر القائمة الجديدة عند تنفيذ طلب GET للقوائم.\n" +
                                "في حال وجود بيانات ناقصة أو خاطئة يجب إرجاع رسالة خطأ مناسبة وكود حالة صحيح (مثل 400).يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق. تهدف هذه الميزة إلى تمكين المستخدم من إضافة قائمة جديدة تحت Board محدد، مع ضمان حفظها بنجاح في قاعدة البيانات وإمكانية استرجاعها من خلال واجهات الـ API المختلفة.\n" +
                                "تُعد خاصية إنشاء القوائم جزءاً أساسياً من بنية التطبيق، حيث تساعد المستخدم في تنظيم مهامه وتوزيع العمل داخل الـ Board. لذلك، فإن التأكد من أن عملية إنشاء القائمة تعمل بشكل صحيح يساهم في تحسين تجربة المستخدم ورفع جودة المنتج.\n" +
                                "\uD83C\uDFAF الهدف الأساسي:\n" +
                                "تمكين المستخدم من إنشاء قائمة جديدة مرتبطة بلوحة معينة بحيث يتم تخزينها بشكل صحيح ويمكن الحصول عليها أو تعديلها أو إدارتها لاحقاً.\n" +
                                "\uD83E\uDDE9 نطاق العمل (Scope):\n" +
                                "تنفيذ طلب POST مسؤول عن إنشاء القائمة الجديدة.\n" +
                                "التحقق من صحة البيانات المرسلة في جسم الطلب (مثل الاسم ومعرّف اللوحة).\n" +
                                "ضمان تخزين القائمة الجديدة في قاعدة البيانات بنجاح.\n" +
                                "التأكد من إمكانية استرجاع القائمة باستخدام طلب GET.\n" +
                                "حفظ الـ ID الناتج عن عملية الإنشاء لاستخدامه في اختبارات أخرى.\n" +
                                "التعامل مع الأخطاء الناتجة عن نقص أو خطأ في البيانات.\n" +
                                "إجراء اختبار يدوي وآلي للتأكد من عمل الخاصية من البداية للنهاية (E2E).\n" +
                                "\uD83D\uDCCC متطلبات الوظيفة (Functional Requirements):\n" +
                                "يجب أن يتمكّن المستخدم من إرسال طلب POST يحتوي على بيانات صحيحة لإنشاء القائمة.\n" +
                                "يجب أن يكون الـ Response يحتوي على كود الحالة 200 وبيانات القائمة الجديدة.\n" +
                                "يجب أن يحتوي الرد على معرّف فريد (ID) يتم توليده تلقائياً.\n" +
                                "يجب أن يكون اسم القائمة مطابقاً للاسم المُرسل في الطلب.\n" +
                                "يجب ربط القائمة بالـ Board الصحيح.\n" +
                                "يجب أن تظهر القائمة الجديدة عند تنفيذ طلب GET للقوائم.\n" +
                                "في حال وجود بيانات ناقصة أو خاطئة يجب إرجاع رسالة خطأ مناسبة وكود حالة صحيح (مثل 400).يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق. تهدف هذه الميزة إلى تمكين المستخدم من إضافة قائمة جديدة تحت Board محدد، مع ضمان حفظها بنجاح في قاعدة البيانات وإمكانية استرجاعها من خلال واجهات الـ API المختلفة.\n" +
                                "تُعد خاصية إنشاء القوائم جزءاً أساسياً من بنية التطبيق، حيث تساعد المستخدم في تنظيم مهامه وتوزيع العمل داخل الـ Board. لذلك، فإن التأكد من أن عملية إنشاء القائمة تعمل بشكل صحيح يساهم في تحسين تجربة المستخدم ورفع جودة المنتج.\n" +
                                "\uD83C\uDFAF الهدف الأساسي:\n" +
                                "تمكين المستخدم من إنشاء قائمة جديدة مرتبطة بلوحة معينة بحيث يتم تخزينها بشكل صحيح ويمكن الحصول عليها أو تعديلها أو إدارتها لاحقاً.\n" +
                                "\uD83E\uDDE9 نطاق العمل (Scope):\n" +
                                "تنفيذ طلب POST مسؤول عن إنشاء القائمة الجديدة.\n" +
                                "التحقق من صحة البيانات المرسلة في جسم الطلب (مثل الاسم ومعرّف اللوحة).\n" +
                                "ضمان تخزين القائمة الجديدة في قاعدة البيانات بنجاح.\n" +
                                "التأكد من إمكانية استرجاع القائمة باستخدام طلب GET.\n" +
                                "حفظ الـ ID الناتج عن عملية الإنشاء لاستخدامه في اختبارات أخرى.\n" +
                                "التعامل مع الأخطاء الناتجة عن نقص أو خطأ في البيانات.\n" +
                                "إجراء اختبار يدوي وآلي للتأكد من عمل الخاصية من البداية للنهاية (E2E).\n" +
                                "\uD83D\uDCCC متطلبات الوظيفة (Functional Requirements):\n" +
                                "يجب أن يتمكّن المستخدم من إرسال طلب POST يحتوي على بيانات صحيحة لإنشاء القائمة.\n" +
                                "يجب أن يكون الـ Response يحتوي على كود الحالة 200 وبيانات القائمة الجديدة.\n" +
                                "يجب أن يحتوي الرد على معرّف فريد (ID) يتم توليده تلقائياً.\n" +
                                "يجب أن يكون اسم القائمة مطابقاً للاسم المُرسل في الطلب.\n" +
                                "يجب ربط القائمة بالـ Board الصحيح.\n" +
                                "يجب أن تظهر القائمة الجديدة عند تنفيذ طلب GET للقوائم.\n" +
                                "في حال وجود بيانات ناقصة أو خاطئة يجب إرجاع رسالة خطأ مناسبة وكود حالة صحيح (مثل 400)."),

                "POST",
                "1/cards"     // Endpoint
        );
        response.then().log().all().assertThat().statusCode(414).header("Server","CloudFront");
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 21)
    public void createchecklistbeforecard () {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idCard",  Config.cardid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",       // HTTP method
                "1/checklists"     // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid value for idCard"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 22)
    public void createcardwithnoname() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idList", Config.listid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "")
                        .queryParam("desc","يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق.  "),

                "POST",
                "1/cards"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo(""))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId))
                .extract().response().asString();
        JsonPath js = response.jsonPath();   // << keep response as Response
        Config.cardid2 = js.getString("id");
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 23)
    public void getcard() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/cards/"+Config.cardid2
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo(""))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 24)
    public void createcard() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idList", Config.listid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "كارت")
                        .queryParam("desc","يتناول هذا الكارت عملية تطوير وتنفيذ واختبار خاصية إنشاء قائمة جديدة داخل التطبيق.  "),

                "POST",
                "1/cards"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("كارت"))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId))
                .extract().response().asString();
        JsonPath js = response.jsonPath();   // << keep response as Response
        Config.cardid = js.getString("id");
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 25)
    public void getcard2() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/cards/"+Config.cardid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("كارت"))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 26)
    public void updatecard() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "newcard")
                        .queryParam("desc","we are making a new project"),

                "PUT",
                "1/cards/"+Config.cardid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newcard"))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 27)
    public void getcardafterupdate() {
        String expectedBoardId = Config.boardId;
        String expectedListId = Config.listid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/cards/"+Config.cardid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newcard"))
                .body("desc", instanceOf(String.class))
                .body("desc.length()", greaterThan(0))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("desc"))
                .body("$", hasKey("closed"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idList"))
                .body("$", hasKey("url"))
                .body("$", hasKey("shortUrl"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("closed", instanceOf(Boolean.class))
                .body("idBoard", instanceOf(String.class))
                .body("idList", instanceOf(String.class))
                .body("url", instanceOf(String.class))
                .body("shortUrl", instanceOf(String.class))
                .body("badges", instanceOf(Map.class))
                .body("checkItemStates", instanceOf(List.class))
                .body("labels", instanceOf(List.class))
                .body("idMembers", instanceOf(List.class))
                .body("badges", instanceOf(Map.class))
                .body("badges", hasKey("attachments"))
                .body("badges.attachments", instanceOf(Integer.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idList", equalTo(expectedListId));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 28)
    public void createchecklistitembeforechecklist() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "item1")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",       // HTTP method
                "1/checklists/" + Config.checklistid + "/checkItems"   // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid id"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 29)
    public void createchecklist() {
        String expectedBoardId = Config.boardId;
        String expectedcardId = Config.cardid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idCard", Config.cardid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "POST",
                "1/checklists"
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("Checklist"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idCard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("checkItems"))
                .body("$", hasKey("limits"))

                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("idCard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("checkItems", instanceOf(List.class))
                .body("limits", instanceOf(Map.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idCard", equalTo(expectedcardId))
                .extract().response().asString();
        JsonPath js = response.jsonPath();
        Config.checklistid = js.getString("id");
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 30)
    public void getchecklist() {
        String expectedBoardId = Config.boardId;
        String expectedcardId = Config.cardid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "GET",
                "1/checklists/"+Config.checklistid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("Checklist"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idCard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("checkItems"))


                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("idCard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("checkItems", instanceOf(List.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idCard", equalTo(expectedcardId));
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 31)
    public void updatechecklist() {
        String expectedBoardId = Config.boardId;
        String expectedcardId = Config.cardid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("name", "newchecklist"),

                "PUT",
                "1/checklists/"+Config.checklistid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newchecklist"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idCard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("checkItems"))


                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("idCard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("checkItems", instanceOf(List.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idCard", equalTo(expectedcardId));
        ResponseValidator.assertJson(response);

    }

    @Test(priority = 32)
    public void getchecklistafterupdate() {
        String expectedBoardId = Config.boardId;
        String expectedcardId = Config.cardid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "GET",
                "1/checklists/"+Config.checklistid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("newchecklist"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("idBoard"))
                .body("$", hasKey("idCard"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("checkItems"))


                // check types
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("idBoard", instanceOf(String.class))
                .body("idCard", instanceOf(String.class))
                .body("pos", instanceOf(Number.class))
                .body("checkItems", instanceOf(List.class))
                .body("idBoard", equalTo(expectedBoardId))
                .body("idCard", equalTo(expectedcardId));
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 33)
    public void createchecklistitemwithnoname() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",
                "1/checklists/" + Config.checklistid + "/checkItems"   // Endpoint
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid value for name"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 34)
    public void createchecklistitemwithemojiasaname() {
        String expectedchecklistid = Config.checklistid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "😂😂")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",       // HTTP method
                "1/checklists/" + Config.checklistid + "/checkItems"   // Endpoint
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("😂😂"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("nameData"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("state"))
                .body("$", hasKey("due"))
                .body("$", hasKey("dueReminder"))
                .body("$", hasKey("idMember"))
                .body("$", hasKey("idChecklist"))
                .body("$", hasKey("limits"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("nameData", instanceOf(Map.class))
                .body("pos", instanceOf(Number.class))
                .body("state", instanceOf(String.class))
                .body("idChecklist", instanceOf(String.class))
                .body("limits", instanceOf(Map.class))
                .body("idChecklist", equalTo(expectedchecklistid))
        .extract().response().asString();
        JsonPath js = response.jsonPath();
        Config.checklistitemid2 = js.getString("id");
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 35)
    public void getchecklistitem() {
        String expectedchecklistid = Config.checklistid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "GET",
                "1/checklists/" + Config.checklistid + "/checkItems/"+Config.checklistitemid2
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("😂😂"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("nameData"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("state"))
                .body("$", hasKey("due"))
                .body("$", hasKey("dueReminder"))
                .body("$", hasKey("idMember"))
                .body("$", hasKey("idChecklist"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("nameData", instanceOf(Map.class))
                .body("pos", instanceOf(Number.class))
                .body("state", instanceOf(String.class))
                .body("idChecklist", instanceOf(String.class))
                .body("idChecklist", equalTo(expectedchecklistid));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 36)
    public void createchecklistitem() {
        String expectedchecklistid = Config.checklistid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "item1")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",
                "1/checklists/" + Config.checklistid + "/checkItems"   // Endpoint
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("item1"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("nameData"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("state"))
                .body("$", hasKey("due"))
                .body("$", hasKey("dueReminder"))
                .body("$", hasKey("idMember"))
                .body("$", hasKey("idChecklist"))
                .body("$", hasKey("limits"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("nameData", instanceOf(Map.class))
                .body("pos", instanceOf(Number.class))
                .body("state", instanceOf(String.class))
                .body("idChecklist", instanceOf(String.class))
                .body("limits", instanceOf(Map.class))
                .body("idChecklist", equalTo(expectedchecklistid))
                .extract().response().asString();
        JsonPath js = response.jsonPath();
        Config.checklistitemid = js.getString("id");
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 37)
    public void getchecklistitem2() {
        String expectedchecklistid = Config.checklistid;

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "GET",
                "1/checklists/" + Config.checklistid + "/checkItems/"+Config.checklistitemid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("name", equalTo("item1"))
                .body("$", hasKey("id"))
                .body("$", hasKey("name"))
                .body("$", hasKey("nameData"))
                .body("$", hasKey("pos"))
                .body("$", hasKey("state"))
                .body("$", hasKey("due"))
                .body("$", hasKey("dueReminder"))
                .body("$", hasKey("idMember"))
                .body("$", hasKey("idChecklist"))
                .body("id", instanceOf(String.class))
                .body("name", instanceOf(String.class))
                .body("nameData", instanceOf(Map.class))
                .body("pos", instanceOf(Number.class))
                .body("state", instanceOf(String.class))
                .body("idChecklist", instanceOf(String.class))
                .body("idChecklist", equalTo(expectedchecklistid));

        ResponseValidator.assertJson(response);

    }
    @Test(priority = 38)
    public void deletechecklistitem() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "DELETE",
                "1/checklists/" + Config.checklistid + "/checkItems/"+Config.checklistitemid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("$", hasKey("limits"));
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 39)
    public void GetCheckitemafterdeletingit() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "GET",
                "1/checklists/" + Config.checklistid + "/checkItems/"+Config.checklistitemid
        );
        response.then().log().all().assertThat().statusCode(404).header("Server",Config.server)
                .body(containsString("model not found"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 40)
    public void deletechecklistitemagain() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "DELETE",
                "1/checklists/" + Config.checklistid + "/checkItems/"+Config.checklistitemid
        );
        response.then().log().all().assertThat().statusCode(400).header("Server",Config.server)
                .body(containsString("invalid check item"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 41)
    public void deletechecklist() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "DELETE",
                "1/checklists/" + Config.checklistid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("$", hasKey("limits"));
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 42)
    public void createchecklistitemwafterdeletingchecklist() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "item1")
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",
                "1/checklists/" + Config.checklistid + "/checkItems"   // Endpoint
        );
        response.then().log().all().assertThat().statusCode(404).header("Server",Config.server)
                .body(containsString("The requested resource was not found."));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 43)
    public void getchecklistafterdeletingit() {

        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),

                "GET",
                "1/checklists/" + Config.checklistid
        );
        response.then().log().all().assertThat().statusCode(404).header("Server", Config.server)
                .body(containsString("The requested resource was not found."));
        ResponseValidator.assertNotJson(response);
    }
    @Test(priority = 44)
    public void deletecard() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "DELETE",
                "1/cards/" + Config.cardid
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("$", hasKey("limits"));
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 45)
    public void createchecklistafterdeletingcard () {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("idCard",  Config.cardid)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "POST",
                "1/checklists"
        );
        response.then().log().all().assertThat().statusCode(401).header("Server",Config.server)
                .body(containsString("unauthorized board requested"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 46)
    public void getcardafterdeletingit() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/cards/"+Config.cardid
        );
        response.then().log().all().assertThat().statusCode(404).header("Server",Config.server)
                .body(containsString("The requested resource was not found."));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 47)
    public void deleteboard() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),
                "DELETE",
                "1/boards/" + Config.boardId
        );
        response.then().log().all().assertThat().statusCode(200).header("Server",Config.server)
                .body("_value",nullValue());
        ResponseValidator.assertJson(response);

    }
    @Test(priority = 48)
    public void getcboardafterdeletingit() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token),


                "GET",
                "1/boards/"+Config.boardId
        );
        response.then().log().all().assertThat().statusCode(404).header("Server",Config.server)
                .body(containsString("The requested resource was not found."));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 49)
    public void createlistafterdeletingboard () {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "my list")
                        .queryParam("idBoard", Config.boardId)
                        .queryParam("key", Config.key)
                        .queryParam("token", Config.token)
                        .queryParam("color", "green"),
                "POST",
                "1/lists"
        );
        response.then().log().all().assertThat().statusCode(401).header("Server",Config.server)
                .body(containsString("unauthorized board list requested"));
        ResponseValidator.assertNotJson(response);

    }
    @Test(priority = 50)
    public void createboardwithoutkeyandtoken() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "ITI")
                        .queryParam("key", "")
                        .queryParam("token", "")
                        .queryParam("desc", "we are making boards for our Iti project"),
                "POST",
                "1/boards"
        );
        response.then().log().all().assertThat().statusCode(401).header("Server",Config.server)

                .body("message", equalTo("missing scopes"));
        ResponseValidator.assertJson(response);
    }
    @Test(priority = 51)
    public void createboardwithoutkey() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "ITI")
                        .queryParam("key", "")
                        .queryParam("token", Config.token)
                        .queryParam("desc", "we are making boards for our Iti project"),
                "POST",
                "1/boards"
        );
        response.then().log().all().assertThat().statusCode(401).header("Server",Config.server)
                .body(containsString("invalid key"));
        ResponseValidator.assertNotJson(response);
    }
    @Test(priority = 52)
    public void createboardwithouttoken() {


        Response response = AllureRestAssured.logAndAttach(
                RestAssured
                        .given().log().all()
                        .queryParam("name", "ITI")
                        .queryParam("key", Config.key)
                        .queryParam("token", "")
                        .queryParam("desc", "we are making boards for our Iti project"),
                "POST",
                "1/boards"
        );
        response.then().log().all().assertThat().statusCode(401).header("Server",Config.server)

                .body("message", equalTo("missing scopes"));
        ResponseValidator.assertJson(response);
    }

}
