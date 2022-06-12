# -*- coding: utf-8 -*-
"""
Created on Thu Jun  9 13:09:23 2022

@author: glanox
"""

import os
import tensorflow as tf
from flask import Flask, request, jsonify
import numpy as np
from tensorflow.keras.applications.inception_v3 import preprocess_input


app = Flask(__name__)
folderImage = './img/'
app.config['folderImage'] = folderImage

batik_model = tf.keras.models.load_model('batik_model/1')

@app.route('/batikMotif', methods=['POST'])
def predict_image():
    files = request.files.getlist('file')
    for file in files:
        file.save(os.path.join(app.config['folderImage'], file.filename))
    image_path = os.path.join(app.config['folderImage'], file.filename)
    image = tf.keras.utils.load_img(image_path, target_size=(150, 150))
    x = tf.keras.utils.img_to_array(image)
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)
    images = np.vstack([x])
    batikMotif_predict = batik_model.predict(images)
    nama = ['Celup', 'Cendrawasih ','Kawung','Parang', 'Tambal']
    batikArray = []

    res = {}
    for (imge, pers) in zip(nama, batikMotif_predict[0]):
        batikArray.append({"Motif Batik": imge, "persentase": round(pers * 100) })
    newlist = sorted(batikArray, key=lambda d: d['persentase'], reverse=True) 
    res.update({"motifBatik": newlist})
    

    respond = jsonify(res)
    respond.status_code = 200
    return respond

if __name__ == '__main__':
    app.run(debug=False, port=80, host='0.0.0.0')