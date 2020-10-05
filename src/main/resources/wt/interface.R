args <- commandArgs(trailingOnly = TRUE)

wt.data.project.path <- args[1]
image.path <- args[2]
clazz <- args[3]
value <- args[4]

setwd(wt.data.project.path)
source("visualization/utils.R")
source("visualization/static_plot.R")
source("visualization/time_animation.R")
source("visualization/time_trend.R")

if (clazz == "aviation" & value == "battles") {
  static.heatmap.default.aviation.battles()
} else if (clazz == "aviation" & value == "win_rate") {
  static.heatmap.default.aviation.win_rate()
} else if (clazz == "ground_vehicles" & value == "battles") {
  static.heatmap.default.ground_vehicles.battles()
} else if (clazz == "ground_vehicles" & value == "win_rate") {
  static.heatmap.default.ground_vehicles.win_rate()
}
ggsave(image.path, width = 7, height = 8, dpi = 75)
