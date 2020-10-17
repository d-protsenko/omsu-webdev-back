from argparse import ArgumentParser
from datetime import datetime
from datetime import timedelta
import random
import string
import codecs
import uuid
import json

datetime_format = '%Y-%m-%dT%H:%M:%S%z'
datetime_format_readable = '%%Y-%%m-%%dT%%H:%%M:%%S%%z'

# example: 2019-12-25T13:21:12+0000

def parse_arguments():
    parser = ArgumentParser(description='fill db with mocked data')
    parser.add_argument('-s', '--start',
                        type=str,
                        dest='period_start',
                        required=True,
                        help='Start of the readings period in this format: ' +
                             datetime_format_readable
                        )
    parser.add_argument('-e', '--end',
                        type=str,
                        dest='period_end',
                        required=True,
                        help='End of the readings period in this format: ' +
                             datetime_format_readable
                        )
    parser.add_argument('--count',
                        type=int,
                        dest='value_count',
                        default=50,
                        help='Number of items to generate, default is 50'
                        )
    parser.add_argument('-o', '--output',
                        type=str,
                        dest='output_file',
                        default='output.txt',
                        help='Path to the output file, default is "output.txt"'
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


def randomString(stringLength: int):
    letters = string.ascii_letters
    return ''.join(random.choice(letters) for i in range(stringLength))


# TODO: update model
def gen_object(id, datetime):
    cores = round(random.random() * 128)
    return {
        'id': str(id),
        "cpuUsage": round(random.random() * 100),
        "ramUsage": round(random.random() * 100),
        "threads": cores * 2,
        "cores": cores,
        "clockPerCore": round(random.random() * 5000),
        "updatedAt": (datetime + timedelta(
            days=round(random.random() * 30))).isoformat(),
    }


def write_sql_value_jsonln(file, json_obj, endsymbol):
    file.write(
        "(\'" + json.dumps(json_obj, ensure_ascii=False) + "\')" + endsymbol)
    file.write(u'\n')


def write_string(file, plain_string):
    file.write(plain_string)


def write_log(file, log):
    for x in range(0, len(log) - 1):
        file.write(
            "(\'" + json.dumps(log[x], ensure_ascii=False) + "\')" + ",")
        file.write(u'\n')
    file.write("(\'" + json.dumps(log[-1], ensure_ascii=False) + "\')" + ";\n")


def write_insert_value_to_collection(file, value, collection):
    write_string(file, 'INSERT INTO ' + collection + '(document) VALUES\n')
    write_sql_value_jsonln(file, value, ';')


def write_obj_log(
        file,
        period_start,
        period_end,
        count: int
):
    step = (period_end - period_start) / count
    # TODO: update table name
    write_string(file, 'INSERT INTO hardware_info(document) VALUES\n')
    for i in range(count - 1):
        object_id = uuid.uuid4()
        print('LOG: Generating obj#' + str(i) + " with id:" + str(object_id))
        datetime = period_start
        generated_object = gen_object(object_id, datetime)
        datetime += step
        write_sql_value_jsonln(file, generated_object, ',')
    object_id = uuid.uuid4()
    datetime = period_end
    generated_object = gen_object(object_id, datetime)
    write_sql_value_jsonln(file, generated_object, ';')


def main():
    args = parse_arguments()
    output = codecs.open(args.output_file, 'w+', 'utf-8')

    write_obj_log(
        file=output,
        period_start=datetime.strptime(args.period_start, datetime_format),
        period_end=datetime.strptime(args.period_end, datetime_format),
        count=args.value_count
    )


if __name__ == '__main__':
    main()
