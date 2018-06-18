package cqk.usst.androidexame.entities;

/**
 * Created by 10033 on 2018/3/27.
 */

public class ThreadInfo {
    private int id;
    private String url;
    private int start;
    private int ended;
    private int finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, int start, int ended, int finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.ended = ended;
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", ended=" + ended +
                ", finished=" + finished +
                '}';
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getEnded() {

        return ended;
    }

    public void setEnded(int ended) {
        this.ended = ended;
    }

    public int getStart() {

        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getUrl() {

        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
