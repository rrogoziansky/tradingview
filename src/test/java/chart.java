import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.*;

public class chart {
    private Config config;
    private CombinePictures combinePictures;

    @Test
    public void chart1() {
        config = new Config();
        combinePictures = new CombinePictures();
//        Configuration.startMaximized = true;
        Configuration.browserSize = "1900x640";
        System.out.println( "" );

        //creating new folder for screenshot results
        Date date = new Date();
        SimpleDateFormat form = new SimpleDateFormat( "dd_MM_yyyy_hh_mm_a" );
        String time = form.format( date );
        String newFolder = config.resultFolderPath + time;
        try {
            Files.createDirectories( Paths.get( newFolder ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println( "Folder is: " + newFolder );


//        boolean savePageSource = Configuration.savePageSource;

        open( "https://charts.cointrader.pro/charts.html?coin=BITCOIN%3AUSD" );
        Cookie ck = new Cookie( "user_id", "dseux02xhf" );
        WebDriverRunner.getWebDriver().manage().addCookie( ck );

        // open pages in order
        int linkQuantity = Array.getLength( config.linksArray );
        for (int i = 0; i < linkQuantity; i++) {
            System.out.println( "======== Start ========" );
//            String currentLink = (String) Array.get( config.linksArray, i );
//            coinPairName = coinPairName.replace( "%3A", "_" )
//            String coinPairName = currentLink.split( "=" )[1];

            String currentLink = config.linksArray[i][0];
            String coinPairName = config.linksArray[i][1];

            open( currentLink );
            $( By.xpath( "//div[@role='progressbar']" ) ).waitUntil( Condition.disappear, 10 * 1000 );
            WebElement iFrame = $( By.xpath( "//iframe[@src[starts-with(., 'tv-lib2') and string-length() > 88]]" ) );
            switchTo().frame( iFrame );
            $( By.xpath( "(//table/tr/td//canvas)[2]" ) ).waitUntil( Condition.visible, 10 * 1000 );
            $( By.xpath( "((//table/tr/td)[3]//canvas)[2]" ) ).waitUntil( Condition.visible, 10 * 1000 );
            sleep( 2 * 1000 );

            int filterQuantity = Array.getLength( config.filtersArray );
            String pathPic1 = null;
            String pathPic2 = null;
            String screenShotName = null;
            int screen;

            for (screen = 0; screen < filterQuantity; screen++) {
                String currentFilter = (String) Array.get( config.filtersArray, screen );

                $( By.xpath( "//*[@id=\"header-toolbar-intervals\"]" ) ).click();
                $( By.xpath( "//div[@data-name=\"menu-inner\"]//div[text()= '" + currentFilter + "']" ) ).click();
                sleep( 5 * 1000 );

                //creating screenshot
                screenShotName = i + "_" + coinPairName + "_" + currentFilter;
                Selenide.screenshot( screenShotName );
                screenShotName = screenShotName + ".png";

                if (screen == 1) { pathPic1 = config.screenshotFilePath + screenShotName; }
                if (screen == 0) { pathPic2 = config.screenshotFilePath + screenShotName; }
            }

            String combinedFile = null;
            // Combine two pictures into one
            try {
                combinedFile = combinePictures.combineTwoPictures( pathPic1, pathPic2, config.screenshotFilePath, coinPairName );
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Copy file from the workspace to the directory with results
            File from = new File( combinedFile );
            File to = new File( newFolder + "\\" + i + "_" + coinPairName + ".png" );
            System.out.println( "Screenshot name is: " + i + "_" + coinPairName + ".png" );
            try {
                FileUtils.copyFile( from, to );
                FileUtils.fileDelete( String.valueOf( from ) );
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println( "========= End =========" );
            System.out.println( "" );
        }
    }

}