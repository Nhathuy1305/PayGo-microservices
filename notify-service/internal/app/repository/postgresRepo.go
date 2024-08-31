package dbrepo

import "notify-service/internal/app/models"

type PostgresRepo interface {
	GetAccountByID(id int) (*models.Account, error)
}
