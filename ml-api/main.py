from flask import Flask, request, jsonify
import tensorflow as tf
import tensorflow_hub as hub
from PIL import Image
import numpy as np
import requests

app = Flask(__name__)

model = tf.keras.models.load_model('model.h5', compile=False)
class_names = ['fres_tea', 'fruit_tea_strawberry', 'nu_tea', 'teh_botol_botol', 'teh_botol_kotak', 'teh_kotak_lemon', 'teh_kotak_ori', 'teh_pucuk']

@app.route('/classify', methods=['POST'])
def classify_image():
    try:
        print("Request JSON:", request.json)
        if 'imageUrl' not in request.json:
            return jsonify({'error': 'No image URL provided'}), 400

        image_url = request.json['imageUrl']

        image = Image.open(requests.get(image_url, stream=True).raw)

        # if image.mode != 'RGB':
        #     return jsonify({'error': 'No object found in the image'}), 400
        
        image = image.resize((224, 224))

        image_array = np.array(image) / 255.0
        image_array = np.expand_dims(image_array, axis=0)
        
        predictions = model.predict(image_array)
        predicted_class_index = np.argmax(predictions)
        predicted_class = class_names[predicted_class_index]
        confidence = np.max(predictions)
        
        
        result = {
            'predictions': predictions.tolist(),
            'prediction_label': int(predicted_class_index),
            'product_name': predicted_class,
            'confidence': float(confidence)
        }

        return jsonify(result), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True, port=8080)