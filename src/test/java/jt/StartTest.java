package jt;

import me.saro.selenium.SeleniumChromeAllInOne;
import me.saro.selenium.model.DownloadStrategy;
import me.saro.selenium.model.Platform;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

@Disabled
public class StartTest {

    @Test
    public void test() {
        var sca = SeleniumChromeAllInOne.builder(new File("./tmp"))
                .enableRecommendChromeOptions(true)
                .build();

        var list = sca.openBackground("https://anissia.net", dp -> {
            var driver = dp.getDriver();
            var items = new ArrayList<>();

            dp.finds(driver, ".flex.items-center.py-3.my-1.border-b.border-gray-200.text-sm.anissia-home-reduce-10").forEach(
                    e -> items.add(dp.find(e, "a").getText())
            );

            return items;
        });

        list.forEach(System.out::println);
    }

    @Test
    public void download() {
        SeleniumChromeAllInOne.download(new File("./tmp"), Platform.getPlatform(), DownloadStrategy.DOWNLOAD_IF_NO_VERSION);
    }
}
