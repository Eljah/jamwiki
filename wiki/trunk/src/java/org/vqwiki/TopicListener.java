/**
 * @author garethc
 *  11/11/2002 11:34:28
 */
package org.vqwiki;

import java.util.EventListener;

/**
 * Interface for listening for topic changes
 */
public interface TopicListener extends EventListener {

    /**
     * Fired when a topic is saved
     * @param event event with information
     */
    public void topicSaved(TopicSavedEvent event);
}
