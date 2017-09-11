package me.moosecanswim.springroboresume.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value="session")
public class PostedJobSession  implements Serializable {
    private PostedJob aPJ;

    public PostedJob getaPJ() {
        return aPJ;
    }

    public void setaPJ(PostedJob aPJ) {
        this.aPJ = aPJ;
    }

}




//public class ShoppingCart implements Serializable {
//    private ArrayList<String> things = new ArrayList<String>();
//    public void addThings(String thing){
//        things.add(thing);
//    }
//    public String getThings(){
//        StringBuilder response= new StringBuilder();
//        for(String thing : things){
//            response.append(thing);
//            response.append(", ");
//        }
//        return response.toString();
//    }
//}