package besteburhan.artibir;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by besteburhan on 15.9.2017.
 */

public class Questions implements Parcelable {
    String userUid;
    String questionIssue;
    String questionExplanation;
    String questionDate;
    String questionCategory;
    Object questionLocationClass;
    String questionPoint;




    public  Questions(){}
    public Questions(String questionCategory, String questionDate,String questionExplanation, String questionIssue,Object questionLocationClass,String questionPoint,String userUid) {
        this.userUid = userUid;
        this.questionIssue = questionIssue;
        this.questionExplanation = questionExplanation;
        this.questionLocationClass= questionLocationClass;
        this.questionDate = questionDate;
        this.questionCategory = questionCategory;
        this.questionPoint = questionPoint;
    }

    protected Questions(Parcel in) {
        userUid = in.readString();
        questionIssue = in.readString();
        questionExplanation = in.readString();
        questionDate = in.readString();
        questionCategory = in.readString();
        questionPoint = in.readString();
    }

    public static final Creator<Questions> CREATOR = new Creator<Questions>() {//daha sonra bakmayı unutma
        @Override
        public Questions createFromParcel(Parcel in) {
            return new Questions(in);
        }

        @Override
        public Questions[] newArray(int size) {
            return new Questions[size];
        }
    };

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getQuestionIssue() {
        return questionIssue;
    }

    public void setQuestionIssue(String questionIssue) {
        this.questionIssue = questionIssue;
    }

    public String getQuestionExplanation() {
        return questionExplanation;
    }

    public void setQuestionExplanation(String questionExplanation) {
        this.questionExplanation = questionExplanation;
    }

    public String getQuestionDate() {
        return questionDate;
    }
    public String getQuestionCategory() {
        return questionCategory;
    }
    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }
    public void setQuestionDate(String questionDate) {
        this.questionDate = questionDate;
    }

    public Object getQuestionLocationClass() {
        return questionLocationClass;
    }

    public void setQuestionLocationClass(Object questionLocationClass) {
        this.questionLocationClass = questionLocationClass;
    }

    public String getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(String questionPoint) {
        this.questionPoint = questionPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getUserUid());
        parcel.writeString(this.getQuestionCategory());
        parcel.writeString(this.getQuestionDate());
        parcel.writeString(this.getQuestionExplanation());
        parcel.writeString(this.getQuestionIssue());
        parcel.writeString(this.getQuestionPoint());
        parcel.writeValue(this.getQuestionLocationClass());//kesin hata vercek valla

    }
}
