package test.books.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BooksTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String SITE_SUBMARINO = "http://www.submarino.com.br";
    private static final String XPATH_BOOK_SUBMARINO = "(//*[starts-with(@alt, 'Livro')])[2]";
    private static final String XPATH_IBSN_SUBMARINO = "//*[@id='content']/main/div[1]/div/div/div[4]/section[2]/div/table/tbody/tr[5]/td[2]";
    private static final String XPATH_AUTHOR_SUBMARINO = "//*[@id='content']/main/div[1]/div/div/div[4]/section[2]/div/table/tbody/tr[3]/td[2]";
    private static final String SITE_EXTRA = "http://www.extra.com.br/";
    private static final String XPATH_AUTHOR_EXTRA = "//*[@id='ctl00_Conteudo_ctl64_DetalhesProduto_rptGrupos_ctl00_rptCampos_ctl02_dlCategoria']/dd";
    private static final String XPATH_SEARCH_BOOK_EXTRA = "//input[starts-with(@id, 'ctl00_')][1]";
    private static final String CONSULT_ISBN_EXTRA = "ISBN+";
    private static final String XPATH_SUBMIT_SEARCH_EXTRA = "//button[starts-with(@id, 'ctl00_')]";
    private static final String XPATH_RESULT_SEARCH_EXTRA = "(//a[starts-with(@title, 'Livro')])[3]";
    private static final String SITE_AMAZON = "www.amazon.com";
    private static final String ID_SEARCH_BOOK_AMAZON = "twotabsearchtextbox";
    private static final String CONSULTA_ISBN_AMAZON = "isbn ";
    private static final String XPATH_SEARCH_BOOK_AMAZON = "//input[contains(@value, 'Go')]";
    private static final String XPATH_RESULT_SEARCH_AMAZON = "(//img[contains(@alt, 'Product Details')])[1]";
    private static final String XPATH_AUTHOR_AMAZON = "//a[contains(@class, 'a-link-normal contributorNameID')]";

    @Before
    public void openBrowser() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/home/brodrigo/Documentos/Ferramentas/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 30);
        driver.navigate().to(SITE_SUBMARINO);
    }

    @Test
    public void compareBooks() throws InterruptedException {

        BookPage book = new BookPage();
        /*
         * Acessando o SITE do SUBMARINO clicando no primeiro livro e pegando as
         * informacoes do ISBN e Autor para futuras
         * comparacoes.
         */
        book.clickBook();

        String ISBN = driver.findElement(By.xpath(XPATH_IBSN_SUBMARINO)).getText();

        String AUTHOR = driver.findElement(By.xpath(XPATH_AUTHOR_SUBMARINO)).getText();

        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(XPATH_IBSN_SUBMARINO)));

        driver.get(SITE_EXTRA);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_SEARCH_BOOK_EXTRA)));

        book.fillIsbnBook(CONSULT_ISBN_EXTRA + ISBN);

        book.clickSearch();

        if (driver.findElement(By.className("lista-busca-compare prateleira")) == null) {
            System.out.println("Não foi possível encontrar o livro desejado.");
            return;
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_RESULT_SEARCH_EXTRA)));

        book.clickResultFirst();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPATH_AUTHOR_EXTRA)));

        String AUTHOREXTRA = driver.findElement(By.xpath(XPATH_AUTHOR_EXTRA)).getText();
        assertEquals(AUTHOR, AUTHOREXTRA); // Realiza comparacao de autores
        // referente ao livro do site do
        // Submarino com o Extra.

        driver.get(SITE_AMAZON);

        wait.until(ExpectedConditions.elementToBeClickable(By.id(ID_SEARCH_BOOK_AMAZON)));

        book.fillIsbnBooks(CONSULTA_ISBN_AMAZON + ISBN).clickSearchTwo();

        if (driver.findElement(By.id("noResultsTitle")) != null) {
            System.out.println("Não foi possível encontrar o livro desejado.");
            return;
        }

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(XPATH_RESULT_SEARCH_AMAZON)));

        book.clickResultFirstTwo();

        String AUTHORAMAZON = driver.findElement(By.xpath(XPATH_AUTHOR_AMAZON)).getText();
        assertEquals(AUTHOR, AUTHORAMAZON); // Realiza comparacao de autores
        // referente ao livro do site do
        // Submarino com o Amazon.
    }

    /**
     * Estou utilizando inner class apenas porque é um teste em uma única
     * classe. O correto seria criar uma classe externa.
     */

    private class BookPage {

        public BookPage clickBook() { // Clica no primeiro livro apresentado na
                                      // página do site Submarino.
            driver.findElement(By.xpath(XPATH_BOOK_SUBMARINO)).click();
            return this;
        }

        public BookPage fillIsbnBook(String name) { // Campo de busca do site
                                                    // Extra.
            driver.findElement(By.xpath(XPATH_SEARCH_BOOK_EXTRA)).clear();
            driver.findElement(By.xpath(XPATH_SEARCH_BOOK_EXTRA)).sendKeys(name);
            return this;
        }

        public BookPage clickSearch() { // Botão que realiza a busca.
            driver.findElement(By.xpath(XPATH_SUBMIT_SEARCH_EXTRA)).click();
            return this;
        }

        public BookPage clickResultFirst() { // Após realizar a busca clica no
                                             // livro encontrado.
            driver.findElement(By.xpath(XPATH_RESULT_SEARCH_EXTRA)).click();
            return this;
        }

        public BookPage fillIsbnBooks(String book) { // Campo de busca do site
                                                     // Amazon.
            driver.findElement(By.id(ID_SEARCH_BOOK_AMAZON)).clear();
            driver.findElement(By.id(ID_SEARCH_BOOK_AMAZON)).sendKeys(book);
            return this;
        }

        public BookPage clickSearchTwo() { // Botão que realiza a busca.
            driver.findElement(By.xpath(XPATH_SEARCH_BOOK_AMAZON)).click();
            return this;
        }

        public BookPage clickResultFirstTwo() {// Após realizar a busca clica no
                                               // livro encontrado.
            driver.findElement(By.xpath(XPATH_RESULT_SEARCH_AMAZON)).click();
            return this;
        }
    }

    @After
    public void afterClass() {
        driver.quit();
    }
}