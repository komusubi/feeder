package org.komusubi.feeder.storage.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.komusubi.feeder.bind.FeederMessage;
import org.komusubi.feeder.model.Message;

public class FilePageCacheTest {
    
    private File file = new File(System.getProperty("java.io.tmpdir") + "/unit-feeder.txt");

    @Test
    public void replaceRefresh() {
        // setup
        FilePageCache cache = new FilePageCache(file);
        Message message = new FeederMessage();
        for (int i = 0; i < 50; i++) {
            message.append("text message message  message  message  message  message  message  message  message  message  message  message  message  message : " + i);
        }
        cache.store(message);

        // exercise
        cache.refresh();
        
        // verify
        List<String> items = cache.cache();
        assertThat(items.size(), is(40));
    }
    
    @Test
    public void stripSchema() {
        // setup
        FilePageCache cache = new FilePageCache(file);
        String stripped = cache.strip("message test http://localhost/test abcdefg");
        assertThat(stripped, is("message test abcdefg"));
    }

    @Test
    public void existsAllMessages() {
        // setup
        final String[] scripts = new String[]{"script no 1.", "script no 2.", "script no 3."};
        
        FilePageCache cache = new FilePageCache(file) {
            @Override
            public List<String> cache() {
                return Arrays.asList(scripts[0], scripts[1], scripts[2]);
            }
        };
        // use FeederMessage, because TweetMessage adjust text length by append method.
        Message message = new FeederMessage();
        message.append(scripts[0])
                .append(scripts[1])
                .append(scripts[2]);
        
        // exercise & verify
        assertTrue(cache.exists(message));
    }
    
    @Test
    public void existsMessages() {
        // setup
        final String[] scripts = new String[]{"tweet message already no 1.", "tweet message already no 2.", "tweet message already no 3."};
        FilePageCache cache = new FilePageCache(file) {
            @Override
            public List<String> cache() {
                return Arrays.asList(scripts[0], scripts[1], scripts[2]);
            }
        };
        // use FeederMessage, because TweetMessage adjust text length by append method.
        Message message = new FeederMessage();
        message.append(scripts[2])
                .append(scripts[0])
                .append(scripts[1]);

        // exercise
        assertTrue(cache.exists(message));
    }
}
