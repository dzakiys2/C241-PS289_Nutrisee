from flask import Flask, request, jsonify
import tensorflow as tf
import tensorflow_hub as hub
from PIL import Image
import numpy as np
import requests

app = Flask(__name__)

model = tf.keras.models.load_model('model.h5', compile=False)

# {'botol_elektrolit_ori_pocari': 0, 'botol_kopi_aren_kopken': 1, 'botol_kopi_cappuccino_golda': 2, 'botol_kopi_susu_abc': 3, 'botol_soda_jeruk_fanta': 4, 'botol_soda_ori_cocacola': 5, 'botol_soda_ori_tebs': 6, 'botol_soda_strawberry_fanta': 7, 'botol_soda_zerosugar_cocacola': 8, 'botol_teh_apel_frestea': 9, 'botol_teh_blackcurrant_fruittea': 10, 'botol_teh_less_sosro': 11, 'botol_teh_less_tehpucuk': 12, 'botol_teh_ori_sosro': 13, 'botol_teh_ori_tehgelas': 14, 'botol_yogurt_mixedfruit_cimory': 15, 'botol_yogurt_ori_cimory': 16, 'cup_mie_ayam_popmie': 17, 'cup_mie_ayambawang_popmie': 18, 'cup_mie_goreng_sedaap': 19, 'cup_mie_kariayam_popmie': 20, 'cup_mie_sotoayam_popmie': 21, 'kaca_nrg_ori_kratingdeng': 22, 'kaleng_soda_limeapel_greensands': 23, 'kaleng_soda_ori_cocacola': 24, 'kaleng_soda_ori_sprite': 25, 'kaleng_soda_sodawater_polaris': 26, 'kaleng_soda_strawberry_fanta': 27, 'kaleng_susu_ori_beruang': 28, 'kotak_jus_apel_abc': 29, 'kotak_jus_jambu_abc': 30, 'kotak_jus_jerukmaduless_nutrisari': 31, 'kotak_jus_jerukperasless_nutrisaari': 32, 'kotak_susu_coklat_ultramilk': 33, 'kotak_susu_fullcream_ultramilk': 34, 'kotak_susu_straberry_ultramilk': 35, 'plastik_biskuit_classic_goodtime': 36, 'plastik_biskuit_doublechoc_goodtime': 37, 'plastik_coklat_dark_silverqueen': 38, 'plastik_coklat_susu_dairymilk': 39, 'plastik_kacang_kupas_duakelinci': 40, 'plastik_mie_ayambawang_indomie': 41, 'plastik_mie_ayambawang_sedaap': 42, 'plastik_mie_ayamkremesisidua_suksess': 43, 'plastik_mie_ayampanggangjumbo_indomie': 44, 'plastik_mie_goreng_indomie': 45, 'plastik_mie_goreng_sedaap': 46, 'plastik_mie_kariayam_indomie': 47, 'plastik_mie_soto_sedaap': 48, 'plastik_mie_sotomie_indomie': 49, 'plastik_mie_sotospesial_indomie': 50, 'plastik_popcorn_ori_poppypop': 51, 'plastik_ringan_keju_twistko': 52, 'zipper_kacang_honeyreasted_mrp': 53}
class_names = [
    'botol_elektrolit_ori_pocari', 'botol_kopi_aren_kopken', 'botol_kopi_cappuccino_golda', 'botol_kopi_susu_abc',
    'botol_soda_jeruk_fanta', 'botol_soda_ori_cocacola', 'botol_soda_ori_tebs', 'botol_soda_strawberry_fanta',
    'botol_soda_zerosugar_cocacola', 'botol_teh_apel_frestea', 'botol_teh_blackcurrant_fruittea', 'botol_teh_less_sosro',
    'botol_teh_less_tehpucuk', 'botol_teh_ori_sosro', 'botol_teh_ori_tehgelas', 'botol_yogurt_mixedfruit_cimory',
    'botol_yogurt_ori_cimory', 'cup_mie_ayam_popmie', 'cup_mie_ayambawang_popmie', 'cup_mie_goreng_sedaap',
    'cup_mie_kariayam_popmie', 'cup_mie_sotoayam_popmie', 'kaca_nrg_ori_kratingdeng', 'kaleng_soda_limeapel_greensands',
    'kaleng_soda_ori_cocacola', 'kaleng_soda_ori_sprite', 'kaleng_soda_sodawater_polaris', 'kaleng_soda_strawberry_fanta',
    'kaleng_susu_ori_beruang', 'kotak_jus_apel_abc', 'kotak_jus_jambu_abc', 'kotak_jus_jerukmaduless_nutrisari',
    'kotak_jus_jerukperasless_nutrisaari', 'kotak_susu_coklat_ultramilk', 'kotak_susu_fullcream_ultramilk',
    'kotak_susu_straberry_ultramilk', 'plastik_biskuit_classic_goodtime', 'plastik_biskuit_doublechoc_goodtime',
    'plastik_coklat_dark_silverqueen', 'plastik_coklat_susu_dairymilk', 'plastik_kacang_kupas_duakelinci',
    'plastik_mie_ayambawang_indomie', 'plastik_mie_ayambawang_sedaap', 'plastik_mie_ayamkremesisidua_suksess',
    'plastik_mie_ayampanggangjumbo_indomie', 'plastik_mie_goreng_indomie', 'plastik_mie_goreng_sedaap',
    'plastik_mie_kariayam_indomie', 'plastik_mie_soto_sedaap', 'plastik_mie_sotomie_indomie', 'plastik_mie_sotospesial_indomie',
    'plastik_popcorn_ori_poppypop', 'plastik_ringan_keju_twistko', 'zipper_kacang_honeyreasted_mrp']

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
