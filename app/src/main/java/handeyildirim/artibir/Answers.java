package besteburhan.artibir;

/**
 * Created by besteburhan on 15.9.2017.
 */

public class Answers {
    String responderUid;
    String respondenceDate;
    String answerMessage;
    String answerPoint;

    public  Answers(){}
    public Answers(String responderUid, String respondenceDate, String answerMessage, String answerPoint) {
        this.responderUid = responderUid;
        this.respondenceDate = respondenceDate;
        this.answerMessage = answerMessage;
        this.answerPoint = answerPoint;
    }

    public String getResponderUid() {
        return responderUid;
    }

    public void setResponderUid(String responderUid) {
        this.responderUid = responderUid;
    }

    public String getRespondenceDate() {
        return respondenceDate;
    }

    public void setRespondenceDate(String respondenceDate) {
        this.respondenceDate = respondenceDate;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public void setAnswerMessage(String answerMessage) {
        this.answerMessage = answerMessage;
    }

    public String getAnswerPoint() {
        return answerPoint;
    }

    public void setAnswerPoint(String answerPoint) {
        this.answerPoint = answerPoint;
    }
}
