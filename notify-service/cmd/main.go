package main

import (
	"os"

	"github.com/spf13/viper"
)

//var appConfig config.AppConfig

var counts int64

func main() {
	env := os.Getenv("ENV")
	config
}

func configParse(env string) {
	x := viper.New()
}
