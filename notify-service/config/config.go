package config

import "notify-service/internal/app/models"

// AppConfig holds the application config
type AppConfig struct {
	MailChan chan models.MailData
}
