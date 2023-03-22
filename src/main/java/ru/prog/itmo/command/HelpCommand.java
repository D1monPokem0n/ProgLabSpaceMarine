package ru.prog.itmo.command;

import ru.prog.itmo.speaker.ConsoleSpeaker;
import ru.prog.itmo.speaker.Speaker;

public class HelpCommand extends ConsoleCommand {
    @Override
    public void execute() {
        super.execute();
        Speaker speaker = new ConsoleSpeaker();
        speaker.speak("""
                help : вывести справку по доступным командам
                info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                add {element} : добавить новый элемент в коллекцию
                update id {element} : обновить значение элемента коллекции, id которого равен заданному
                remove_by_id id : удалить элемент из коллекции по его id
                clear : очистить коллекцию
                save : сохранить коллекцию в файл
                execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                exit : завершить программу (без сохранения в файл)
                add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
                history : вывести последние 14 команд (без их аргументов)
                remove_any_by_chapter chapter : удалить из коллекции один элемент, значение поля chapter которого эквивалентно заданному
                max_by_melee_weapon : вывести любой объект из коллекции, значение поля meleeWeapon которого является максимальным
                print_field_descending_health : вывести значения поля health всех элементов в порядке убывания
                """);
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}
