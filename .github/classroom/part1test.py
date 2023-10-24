# pylint: disable=missing-function-docstring,missing-module-docstring
import sys


def fail(message: str) -> None:
    print(message)
    sys.exit(1)


def find_line(lines: list[str], name: str):
    for i, line in enumerate(lines):
        if name in line:
            return i, line

    return fail(f'{name} should be in the output, but it was not')


def check_order(lines: list[str], artist1: str, artist2: str) -> None:
    i, _ = find_line(lines, artist1)
    j, _ = find_line(lines, artist2)

    if j < i:
        fail(f'"{artist1}" should be printed before "{artist2}"')


def check_ids(input_lines: list[str]):
    _, line = find_line(input_lines, 'Metallica')

    if '50' not in line:
        fail(f'The ArtistId of Metallica (50) should be included, but the line was "{line}"')


def check_conditions(input_lines: list[str]):
    check_ids(input_lines)

    check_order(input_lines, 'AC/DC', 'Metallica')
    check_order(input_lines, 'Aaron Goldberg', 'Alanis Morissette')
    check_order(input_lines, 'Aaron Copland & London Symphony Orchestra', 'Metallica')


def main():
    input_lines = sys.stdin.readlines()
    input_lines = [line.strip() for line in input_lines]

    check_conditions(input_lines)


main()
