import requests
import psutil

biGiga = 1024 ** 3
ram_url = 'http://webdev.local/api/sensors/ram/create'
cpu_url = 'http://webdev.local/api/sensors/cpu/create'


def build_ram_data():
    return {
        "total": psutil.virtual_memory().total / biGiga,
        "available": psutil.virtual_memory().available / biGiga,
        "free": psutil.virtual_memory().available / biGiga,
        "used": psutil.virtual_memory().used / biGiga,
    }


def build_cpu_data():
    return {
        "threads": psutil.cpu_count(),
        "cores": psutil.cpu_count(False),
        "clock": psutil.cpu_freq().max,
        "cpuUsage": psutil.cpu_percent(interval=1),
    }


def main():
    cpu_data = build_cpu_data()
    ram_data = build_ram_data()
    print('Preparing request data:')
    print('CPU data:')
    print(cpu_data)
    print('RAM data:')
    print(ram_data)
    print('Sending requests...')
    cpu_response = requests.post(cpu_url, json=cpu_data)
    ram_response = requests.post(ram_url, json=ram_data)

    print('cpu response data:')
    print('status code: ' + str(cpu_response.status_code))
    print('body: ' + str(cpu_response.json()))

    print('ram response data:')
    print('status code: ' + str(ram_response.status_code))
    print('body: ' + str(ram_response.json()))


if __name__ == '__main__':
    main()
