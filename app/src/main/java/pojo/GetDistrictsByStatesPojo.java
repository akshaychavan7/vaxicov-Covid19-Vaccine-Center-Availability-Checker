package pojo;

/**
 * Created by Akshay Chavan on 08,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetDistrictsByStatesPojo {

    @SerializedName("districts")
    @Expose
    private List<District> districts = null;
    @SerializedName("ttl")
    @Expose
    private Integer ttl;

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

}
