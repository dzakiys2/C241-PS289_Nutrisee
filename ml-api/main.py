from flask import Flask, request, jsonify
import tensorflow as tf
import tensorflow_hub as hub
from PIL import Image
import numpy as np
import requests

app = Flask(__name__)

model = tf.keras.models.load_model('model.h5', compile=False)
class_names = ['kaleng_soda_limeapel_greensands', 'plastik_mie_kariayam_indomie', 'plastik_biskuit_classic_goodtime', 'kaleng_susu_ori_beruang', 'botol_teh_less_tehpucuk', 'botol_yogurt_ori_cimory', 'kaca_nrg_ori_kratingdeng', 'kotak_susu_fullcream_ultramilk',
               'cup_mie_goreng_sedaap', 'botol_kopi_aren_kopken', 'botol_soda_ori_tebs', 'kaleng_soda_strawberry_fanta', 'plastik_mie_sotomie_indomie', 'botol_teh_ori_tehgelas', 'plastik_mie_sotospesial_indomie', 'kotak_jus_jambu_abc', 'kotak_jus_jerukperasless_nutrisaari',
               'plastik_coklat_susu_dairymilk', 'botol_teh_blackcurrant_fruittea', 'plastik_mie_goreng_sedaap', 'botol_kopi_cappuccino_golda', 'kotak_jus_apel_abc', 'plastik_kacang_kupas_duakelinci', 'botol_teh_ori_sosro', 'plastik_coklat_dark_silverqueen',
               'plastik_mie_ayambawang_sedaap', 'kaleng_soda_ori_sprite', 'plastik_mie_ayamkremesisidua_suksess', 'botol_soda_ori_cocacola', 'plastik_mie_ayambawang_indomie', 'botol_soda_zerosugar_cocacola', 'plastik_mie_soto_sedaap', 'botol_teh_less_sosro', 'kotak_susu_straberry_ultramilk',
               'plastik_biskuit_doublechoc_goodtime', 'cup_mie_sotoayam_popmie', 'kotak_susu_coklat_ultramilk', 'botol_teh_apel_frestea', 'botol_kopi_susu_abc', 'cup_mie_kariayam_popmie', 'botol_elektrolit_ori_pocari', 'cup_mie_ayam_popmie', 'zipper_kacang_honeyreasted_mrp', 'cup_mie_ayambawang_popmie',
               'plastik_ringan_keju_twistko', 'plastik_popcorn_ori_poppypop', 'kaleng_soda_ori_cocacola', 'plastik_mie_goreng_indomie', 'botol_soda_strawberry_fanta', 'botol_yogurt_mixedfruit_cimory', 'kotak_jus_jerukmaduless_nutrisari', 'kaleng_soda_sodawater_polaris', 'plastik_mie_ayampanggangjumbo_indomie', 'botol_soda_jeruk_fanta']

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
