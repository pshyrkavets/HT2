import java.util.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

public class TestJenkins {
    private String base_url = "http://localhost:8080/";
    private StringBuffer verificationErrors = new StringBuffer();
    private WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Sony\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get(base_url);
        driver.findElement(By.name("j_username")).sendKeys("Pavel");
        driver.findElement(By.name("j_password")).sendKeys("12345Epam");
        driver.findElement(By.xpath("//input[@name='Submit' and @value='Sign in']")).click();
    }


    //1.После клика по ссылке «Manage Jenkins» на странице появляется элемент dt с текстом «Manage Users» и
    //элемент dd с текстом «Create/delete/modify users that can log in to this Jenkins».
    @Test
    public void manageJenkinsTest() {
        driver.get(base_url);
        driver.findElement(By.linkText("Настроить Jenkins")).click();
        Assert.assertEquals(driver.findElement(
                By.xpath("//*[@id=\"main-panel\"]/div[16]/a/dl/dt")).getText(), "Управление пользователями");
        //слово модификация написано как модификция в источнике
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/div[16]/a/dl/dd[1]")).getText(),
                "Создание, удаление и модификция пользователей, имеющих право доступа к Jenkins");
    }


    //2.После клика по ссылке, внутри которой содержится элемент dt с текстом «Manage Users», становится доступна
    //ссылка «Create User».
    @Test
    public void manageUsersTest() {
        driver.get(base_url);
        driver.findElement(By.linkText("Настроить Jenkins")).click();
        Assert.assertEquals(driver.findElement(
                By.xpath("//*[@id=\"main-panel\"]/div[16]/a/dl/dt")).getText(), "Управление пользователями");
        driver.findElement(By.xpath("//*[@id=\"main-panel\"]/div[16]/a")).click();
        driver.findElement(By.linkText("Создать пользователя")).click();
    }


    //3.После клика по ссылке «Create User» появляется форма с тремя полями типа text и двумя полями типа password,
    //причём все поля должны быть пустыми.
    @Test
    public void createUserTest() {
        driver.get(base_url + "securityRealm/");
        driver.findElement(By.linkText("Создать пользователя")).click();
        Collection<WebElement> forms = driver.findElements(By.tagName("form"));

        Assert.assertFalse(forms.isEmpty(), "No forms found!");

        Iterator<WebElement> iterator = forms.iterator();
        boolean form_found = false;
        WebElement form;

        while (iterator.hasNext()) {
            form = iterator.next();

            if((form.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[1]/td[2]/input[1]")).
                    getAttribute("type").equalsIgnoreCase("text")) &&
                    (form.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[2]/td[2]/input[1]")).
                            getAttribute("type").equalsIgnoreCase("password")) &&
                    (form.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[3]/td[2]/input[1]")).
                            getAttribute("type").equalsIgnoreCase("password")) &&
                    (form.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[4]/td[2]/input[1]")).
                            getAttribute("type").equalsIgnoreCase("text")) &&
                    (form.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[5]/td[2]/input[1]")).
                            getAttribute("type").equalsIgnoreCase("text"))) {
                        form_found = true;
                        break;
            }
        }

        Assert.assertTrue(form_found,"No suitable forms found!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[1]/td[2]/input[1]")).
                getText(), "","Field is not empty!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[2]/td[2]/input[1]")).
                getText(),"","Field is not empty!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[3]/td[2]/input[1]")).
                getText(),"","Field is not empty!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[4]/td[2]/input[1]")).
                getText(),"","Field is not empty!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[5]/td[2]/input[1]")).
                getText(),"","Field is not empty!");
    }


    //4.После заполнения полей формы («Username» = «someuser», «Password» = «somepassword»,
    //«Confirm password» = «somepassword», «Full name» = «Some Full Name», «E-mail address» = «some@addr.dom») и клика
    //по кнопке с надписью «Create User» на странице появляется строка таблицы (элемент tr), в которой есть ячейка
    //(элемент td) с текстом «someuser».
    @Test
    public void createUserUsingPageObjectTest() {
        // 1-действие: "Открыть http://localhost:8080/securityRealm/addUser"
        driver.get(base_url + "securityRealm/addUser");

        // С этого момента можно использовать PajeObject.
        // С применением PageFactory это выглядит так:
        PageObjectOfCreateUser page = PageFactory.initElements(driver, PageObjectOfCreateUser.class);

        // 1-проверка: "Страница содержит форму с полями «Имя пользователя», «Пароль», «Повторите пароль», «Ф.И.О.»,
        // «Адрес электронной почты» и кнопкой отправки данных «Создать пользователя». Также на странице есть
        // соответствующие текстовые надписи."
        Assert.assertTrue(page.isFormPresentForReal(), "No suitable forms found!");
        verificationErrors.append(page.getErrorOnTextAbsence("Имя пользователя"));
        verificationErrors.append(page.getErrorOnTextAbsence("Пароль"));
        verificationErrors.append(page.getErrorOnTextAbsence("Повторите пароль"));
        verificationErrors.append(page.getErrorOnTextAbsence("Ф.И.О."));
        verificationErrors.append(page.getErrorOnTextAbsence("Адрес электронной почты"));
        verificationErrors.append(page.getErrorOnTextAbsence("Создать пользователя"));

        // 2-действие: "В поле «Имя пользователя» ввести «someuser»."
        page.setUsername("someuser");

        // 2-проверка: "Значение появляется в поле."
        Assert.assertEquals(page.getUsername(), "someuser", "Unable to fill 'Имя пользователя' field");


        // 3-действие: "В поле «Пароль» ввести «somepassword»."
        page.setPassword1("somepassword");

        // 3-проверка: "Значение появляется в поле."
        Assert.assertEquals(page.getPassword1(), "somepassword", "Unable to fill 'Пароль' field");


        // 4-действие: "В поле «Повторите пароль» ввести «somepassword»."
        page.setPassword2("somepassword");

        // 4-проверка: "Значение появляется в поле."
        Assert.assertEquals(page.getPassword2(), "somepassword", "Unable to fill 'Повторите пароль' field");


        // 5-действие: "В поле «Ф.И.О.» ввести «Some Full Name»."
        page.setFullname("Some Full Name");

        // 5-проверка: "Значение появляется в поле."
        Assert.assertEquals(page.getFullname(), "Some Full Name", "Unable to fill 'Ф.И.О.' field");

        // 6-действие: "В поле «Адрес электронной почты» ввести «some@addr.dom»."
        page.setEmail("some@addr.dom");

        // 6-проверка: "Значение появляется в поле."
        Assert.assertEquals(page.getEmail(), "some@addr.dom", "Unable to fill 'Адрес электронной почты' field");

        // 7-действие: "Нажать «Создать пользователя»."
        page.submitForm();

        // 7-проверка: "На странице появляется строка таблицы (элемент tr), в которой есть ячейка (элемент td) с
        // текстом «someuser»."
        Assert.assertTrue(page.getTdWithTextSomeuser(), "Unable to create a new user!");
    }


    //5.После клика по ссылке с атрибутом href равным «user/someuser/delete» появляется текст «Are you sure about
    //deleting the user from Jenkins?».
    @Test
    public void deleteUserTest() {
        driver.findElement(By.linkText("someuser")).click();
        driver.findElement(By.linkText("Удалить")).click();

        Assert.assertTrue(driver.findElement(By.xpath("/html/body/div[4]/div[2]/form")).
                        getText().contains("Вы уверены, что хотите удалить пользователя из Jenkins?"),
                "The text 'Вы уверены, что хотите удалить пользователя из Jenkins?' is not found!");
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            Assert.fail(verificationErrorString);
        }
    }
}
