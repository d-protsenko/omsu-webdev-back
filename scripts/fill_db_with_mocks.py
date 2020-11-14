from argparse import ArgumentParser
from datetime import datetime
from datetime import timedelta
import random
import json
import uuid

datetime_format = '%Y-%m-%dT%H:%M:%S%z'
datetime_format_readable = '%%Y-%%m-%%dT%%H:%%M:%%S%%z'


# example: 2019-12-25T13:21:12+0000

def parse_arguments():
    parser = ArgumentParser(description='fill db with mocked data')
    parser.add_argument('-s', '--start',
                        type=str,
                        dest='period_start',
                        default='2020-11-13T11:00:00Z',
                        help='Start of the readings period in this format: ' +
                             datetime_format_readable
                        )
    parser.add_argument('-e', '--end',
                        type=str,
                        dest='period_end',
                        default='2020-11-13T18:00:00Z',
                        help='End of the readings period in this format: ' +
                             datetime_format_readable
                        )
    parser.add_argument('--count',
                        type=int,
                        dest='value_count',
                        default=50,
                        help='Number of items to generate, default is 50'
                        )

    return parser.parse_args()


def gen_datetimes(min_datetime: datetime, max_datetime: datetime, count: int):
    datetimes = []
    step = (max_datetime - min_datetime) / count

    accumulator = min_datetime
    for i in range(count - 1):
        datetimes.append(accumulator)
        accumulator += step
    datetimes.append(max_datetime)

    return datetimes


def gen_cpu_data(date_time):
    cores = round(random.random() * 128)
    return {
        "id": str(uuid.uuid4()),
        "threads": cores * 2,
        "cores": cores,
        "clock": round(random.random() * 5000),
        "cpuUsage": round(random.random() * 100),
        "updatedAt": date_time,
    }


def gen_ram_data(date_time):
    total = round(random.random() * 100)
    available = round(random.random() * total)
    free = total - available
    return {
        "id": str(uuid.uuid4()),
        "total": total,
        "available": available,
        "free": free,
        "used": free,
        "updatedAt": date_time,
    }


def gen_log_ram_data(data):
    return {
        "id": str(uuid.uuid4()),
        "message": 'Inserting a RAM info gathered from remote machine. RAM usage: ' + str(data['used']),
        "updatedAt": data['updatedAt'],
    }


def gen_log_cpu_data(data):
    return {
        "id": str(uuid.uuid4()),
        "message": 'Inserting a CPU info gathered from remote machine. CPU usage: ' + str(data['cpuUsage']),
        "updatedAt": data['updatedAt'],
    }


def get_sql_value_json(json_obj):
    return "(\'" + json.dumps(json_obj, ensure_ascii=False) + "\')"


def write_insert_value_to_collection(value, collection):
    return 'INSERT INTO ' + collection + '(data) VALUES ' + get_sql_value_json(value) + ';'


def write_obj_log(
        period_start,
        period_end,
        count: int
):
    step = (period_end - period_start) / count
    temp_datetime = period_start
    (period_start + timedelta(days=round(random.random() * 30))).isoformat()
    for i in range(count):
        generated_cpu_object = gen_cpu_data(temp_datetime.isoformat())
        generated_ram_object = gen_ram_data(temp_datetime.isoformat())
        cpu_log_obj = gen_log_cpu_data(generated_cpu_object)
        ram_log_obj = gen_log_ram_data(generated_ram_object)
        temp_datetime += step
        print(write_insert_value_to_collection(generated_cpu_object, 'cpu_info'))
        print(write_insert_value_to_collection(generated_ram_object, 'ram_info'))
        print(write_insert_value_to_collection(cpu_log_obj, 'logging_info'))
        print(write_insert_value_to_collection(ram_log_obj, 'logging_info'))


def main():
    args = parse_arguments()

    write_obj_log(
        period_start=datetime.strptime(args.period_start, datetime_format),
        period_end=datetime.strptime(args.period_end, datetime_format),
        count=args.value_count
    )


if __name__ == '__main__':
    main()
