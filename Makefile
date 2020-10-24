clear_db:
	echo "delete from cpu_info; delete from ram_info; delete from logging_info;" | docker exec -i webdev_postgres psql -U webdev_user webdev

fill_db_with_mocks:
	python3.8 scripts/fill_db_with_mocks.py | docker exec -i webdev_postgres psql -U webdev_user webdev

call_api:
	python3.8 scripts/generate_hw_data.py