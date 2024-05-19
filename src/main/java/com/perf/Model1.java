

package com.perf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import org.apache.commons.math3.stat.regression.ArimaModel;
public class Model1 {

    private ArimaModel model;

    public Model1() {
        try {
            // Load the trained ARIMA model from a PKL file
            FileInputStream fileInputStream = new FileInputStream("arima_model.pkl");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            model = (ArimaModel) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public double predict(List<Double> data) {
        // Make a prediction using the ARIMA model
        return model.predict(data);
    }
}
