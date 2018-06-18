package cqk.usst.androidexame.database;


import java.util.List;

import cqk.usst.androidexame.entities.ThreadInfo;

/**
 * Created by 10033 on 2018/3/28.
 */

public interface ThreadDAO {

    public void insertThread(ThreadInfo threadInfo);

    public void deleteThread(String url, int thread_id);

    public void updataThread(String url, int thread_id, int finished);

    public List<ThreadInfo> getThreads(String url);

    public boolean isExists(String url, int thread_id);
}
