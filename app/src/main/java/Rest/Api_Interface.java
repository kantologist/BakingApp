package Rest;

import java.util.List;

import Models.Recipe;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by femi on 9/6/17.
 */

public interface Api_Interface {

    @GET("baking.json")
    Call<List<Recipe>> getRecipe();
}
