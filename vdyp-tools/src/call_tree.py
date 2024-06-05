import os
import sys
import re
import commons
import argparse

subroutine_declaration_re = re.compile(r'^\s+SUBROUTINE\s+([A-Z0-9_]+)\s*\(')
end_subroutine_declaration_re = re.compile(r'^\s+END SUBROUTINE')
token_re = re.compile(r'CALL\s+([A-Z][A-Z0-9_]*)')

subroutines = {}


def collect_symbols():
    folders = ['C:/source/vdyp/VDYP_Master/Source']
    source_files = []
    while len(folders) > 0:
        current_directory = folders.pop()
        entities = os.scandir(current_directory)
        for e in entities:
            if e.is_dir():
                folders.append(e)
            elif e.is_file():
                if e.name.endswith('.for'):
                    source_files.append(e)

    print('Saw ' + str(len(source_files)) + ' source files')

    for s in source_files:
        commons_details = commons.gather_commons_details(s)
        sf = open(s)
        for line in sf.readlines():
            line = line.upper()
            if line[0] == 'C':
                continue
            m = re.search(subroutine_declaration_re, line)
            if m is not None:
                routine_name = m.group(1)
                subroutines[m.group(1)] = (s, False, commons.gather_commons_usages(s, commons_details, routine_name))
        sf.close()


def generate_call_tree(routines_to_scan):
    if len(routines_to_scan) == 0:
        return

    routine_name = routines_to_scan.pop()

    if subroutines[routine_name][1]:
        # already scanned
        return

    subroutines[routine_name] = (subroutines[routine_name][0], True, subroutines[routine_name][2], set())

    subroutine = subroutines[routine_name]
    file = subroutine[0]

    sf = open(file)
    in_routine = False
    for line in sf.readlines():
        line = line.upper()
        if len(line.strip()) == 0 or line[0] == 'C' or line[0] == '#':
            continue
        m = re.search(subroutine_declaration_re, line)
        if m is not None and m.group(1) == routine_name:
            in_routine = True
        else:
            m = re.search(end_subroutine_declaration_re, line)
            if m is not None:
                in_routine = False
            elif in_routine:
                # tokenize the line, looking for nested calls
                m = re.search(token_re, line)
                while m is not None:
                    token = m.group(1)
                    if token in subroutines:
                        subroutine[3].add(token)
                        if not subroutines[token][1]:
                            routines_to_scan.append(token)
                    line = line[m.end():]
                    m = re.search(token_re, line)


ignored_commons = {'UNITS', 'UNITS3', 'UNITS4'}


def print_call_tree(name, details, blocks_only, assignments_only, indent):
    commons_usages = details[2]

    assignments = ""
    assignments_per_block = {}
    for c in commons_usages["block_assignments"]:
        if c not in ignored_commons:
            assignments_per_block[c] = []
    for m in commons_usages["member_assignments"]:
        c = m[0:m.find('.')]
        if c not in ignored_commons:
            assignments_per_block[c].append(m[len(c) + 1:])
    for c in assignments_per_block.keys():
        assignments += c + '('
        if not blocks_only:
            for m in assignments_per_block[c]:
                assignments += m + ', '
            assignments = assignments[0:len(assignments) - 2]
        assignments += '), '
    assignments = ' assignments: ' + assignments[0:len(assignments) - 2] if len(assignments) > 0 else assignments

    usages = ""
    if not assignments_only:
        usages_per_block = {}
        for c in commons_usages["block_usages"]:
            if c not in ignored_commons:
                usages_per_block[c] = []
        for m in commons_usages["member_usages"]:
            c = m[0:m.find('.')]
            if c not in ignored_commons:
                usages_per_block[c].append(m[len(c) + 1:])
        for c in usages_per_block.keys():
            usages += c + '('
            if not blocks_only:
                for m in usages_per_block[c]:
                    usages += m + ', '
                usages = usages[0:len(usages) - 2]
            usages += '), '
        usages = ' usages: ' + usages[0:len(usages) - 2] if len(usages) > 0 else usages

    print((' ' * indent if indent > 0 else '') + name + assignments + usages)
    for callee in details[3]:
        print_call_tree(callee, subroutines[callee], blocks_only, assignments_only, indent + 4)


def call_tree(routine_name, blocks_only, assignments_only):

    routine_name = routine_name.upper()

    collect_symbols()

    if routine_name not in subroutines:
        print('Subroutine "' + routine_name + '" not found in the source code')
        return

    print('Listing call tree rooted at ' + routine_name)

    routines_to_scan = [routine_name]

    while len(routines_to_scan) > 0:
        generate_call_tree(routines_to_scan)

    print_call_tree(routine_name, subroutines[routine_name], blocks_only, assignments_only, 0)


if __name__ == '__main__':

    parser = argparse.ArgumentParser('Lists the call tree rooted at the given subroutine name'
                                     ' and, for each method, common block usage')
    parser.add_argument('-b', '--blocks-only', action='store_true', help='display block level information only')
    parser.add_argument('-a', '--assignments-only', action='store_true', help='display assignment information only')
    parser.add_argument('subroutine_name', nargs=1)

    args = parser.parse_args(sys.argv[1:])

    call_tree(args.subroutine_name[0], args.blocks_only, args.assignments_only)
