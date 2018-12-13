import java.util.Collection;
import java.util.Iterator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObjectOfCreateUser {
    private WebDriverWait wait;
    private final WebDriver driver;

    // Подготовка элементов страницы.
    @FindBy(xpath = "//*[@id=\"jenkins\"]")
    private WebElement body;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form")
    private WebElement form;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[1]/td[2]/input[1]")
    private WebElement username;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[2]/td[2]/input[1]")
    private WebElement password1;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[3]/td[2]/input[1]")
    private WebElement password2;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[4]/td[2]/input[1]")
    private WebElement fullname;

    @FindBy(xpath = "//*[@id=\"main-panel\"]/form/div[1]/table/tbody/tr[5]/td[2]/input[1]")
    private WebElement email;

    @FindBy(xpath = "//*[@id=\"yui-gen2-button\"]")
    private WebElement submit_button_create_user;

    public PageObjectOfCreateUser(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(this.driver, 60);

        // Провекрка того факта, что мы на верной странице.
        if ((!driver.getTitle().equals("Создать пользователя [Jenkins]")) ||
                (!driver.getCurrentUrl().equals("http://localhost:8080/securityRealm/addUser"))) {
            throw new IllegalStateException("Wrong site page!");
        }
    }

    // Заполнение имени пользователя.
    public PageObjectOfCreateUser setUsername(String value) {
        username.clear();
        username.sendKeys(value);
        return this;
    }

    // Заполнение пароля.
    public PageObjectOfCreateUser setPassword1(String value) {
        password1.clear();
        password1.sendKeys(value);
        return this;
    }

    // Заполнение повторения пароля.
    public PageObjectOfCreateUser setPassword2(String value) {
        password2.clear();
        password2.sendKeys(value);
        return this;
    }

    // Заполнение Ф.И.О.
    public PageObjectOfCreateUser setFullname(String value) {
        fullname.clear();
        fullname.sendKeys(value);
        return this;
    }

    // Заполнение адреса электронной почты.
    public PageObjectOfCreateUser setEmail(String value) {
        email.clear();
        email.sendKeys(value);
        return this;
    }

    // Заполнение всех полей формы.
    public PageObjectOfCreateUser setFields(String username, String password1, String password2, String fullname,
                                            String email) {
        setUsername(username);
        setPassword1(password1);
        setPassword2(password2);
        setFullname(fullname);
        setEmail(email);
        return this;
    }

    // Отправка данных из формы.
    public PageObjectOfCreateUser submitForm() {
        submit_button_create_user.click();
        return this;
    }

    // Надёжный поиск формы.
    public boolean isFormPresentForReal() {
        // Первое (самое правильное) решение (работает примерно в 30-50% случаев)
        // wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//html/body"), 1));

        // Второе (самое интересное) решение (работает примерно в 20-30% случаев; не работает в 3.3.1)
        // waitForLoad(driver);

        // Третье (самое убогое, почти за гранью запрещённого) решение -- работает в 100% случаев

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Collection<WebElement> forms = driver.findElements(By.tagName("form"));
        if (forms.isEmpty()) {
            return false;
        }

        Iterator<WebElement> i = forms.iterator();
        boolean form_found = false;
        WebElement form;

        while (i.hasNext()) {
            form = i.next();

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

        return form_found;
    }

    // Проверка вхождения подстроки в текст страницы.
    public boolean pageTextContains(String search_string) {
        return body.getText().contains(search_string);
    }

    // Получение значения имени пользователя.
    public String getUsername() {
        return username.getAttribute("value");
    }

    // Получение значения пароля.
    public String getPassword1() {
        return password1.getAttribute("value");
    }

    // Получение значения повторения пароля.
    public String getPassword2() {
        return password2.getAttribute("value");
    }

    // Получение значения Ф.И.О.
    public String getFullname() {
        return fullname.getAttribute("value");
    }

    // Получение значения адреса электронной почты.
    public String getEmail() {
        return email.getAttribute("value");
    }

    public String getErrorOnTextAbsence(String search_string) {
        if (!pageTextContains(search_string)) {
            return "No '" + search_string + "' is found inside page text!\n";
        } else {
            return "";
        }
    }

    //Проверка на существование ячейки c текстом «someuser» из строки таблицы после заполнения полей формы и
    // после клика по кнопке с надписью «Создать пользователя».
    public boolean getTdWithTextSomeuser() {
        // Провекрка того факта, что мы на верной странице.
        if ((!driver.getTitle().equals("Пользователи [Jenkins]")) ||
                (!driver.getCurrentUrl().equals("http://localhost:8080/securityRealm/"))) {
            throw new IllegalStateException("Wrong site page!");
        }

        driver.getCurrentUrl();
        boolean td_with_someuser_found = false;
        WebElement theTable = driver.findElement(By.xpath("//*[@id=\"people\"]"));

        if (theTable.findElement(By.linkText("someuser")) != null) {
            td_with_someuser_found = true;
        }

        return td_with_someuser_found;
    }
}
