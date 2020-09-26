import sys

from os.path import join
from deepdanbooru.commands.evaluate import evaluate

_, target_path, = sys.argv

model_path = join("src", "main", "resources", "deepdanbooru-master", "model", "model-resnet_custom_v4.h5")
tags_path = join("src", "main", "resources", "deepdanbooru-master", "model", "tags.txt")

evaluate(target_paths=[target_path],
         project_path=None,
         model_path=model_path,
         tags_path=tags_path,
         threshold=0.5,
         allow_gpu=False,
         compile_model=False,
         allow_folder=False,
         folder_filters=None,
         verbose=False)
