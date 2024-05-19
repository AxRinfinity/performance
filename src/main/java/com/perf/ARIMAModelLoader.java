package com.perf;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.ModelEvaluator;
import org.spark_project.dmg.pmml.FieldName;

public class ARIMAModelLoader {
    private Evaluator evaluator;

    public void loadModel(String modelFilePath) throws Exception {
        // Create a ModelEvaluator from the file
        ModelEvaluator<?> modelEvaluator = new LoadingModelEvaluatorBuilder()
                .load(new File(modelFilePath))
                .build();

        // Convert the ModelEvaluator to an Evaluator
        this.evaluator = (Evaluator) modelEvaluator;
    }

    public double predict(List<Double> data) throws Exception {
        // Prepare input data for evaluation
        Map<InputField, Object> arguments = new LinkedHashMap<>();
        List<? extends InputField> inputFields = this.evaluator.getInputFields();
        for (int i = 0; i < inputFields.size(); i++) {
            InputField inputField = inputFields.get(i);
            arguments.put(inputField, data.get(i));
        }

        // Evaluate the input data and get the prediction
        Map<FieldName, ?> results = this.evaluator.evaluate(arguments);
        FieldName targetFieldName = this.evaluator.getTargetFields().get(0).getName();
        Object prediction = results.get(targetFieldName);

        return (double) prediction;
    }
}
