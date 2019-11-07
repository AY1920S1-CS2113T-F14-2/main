package logic.commands;

import farmio.exceptions.FarmioFatalException;
import farmio.Farmio;
import farmio.exceptions.FarmioException;

public abstract class Command {
    public abstract void execute(Farmio farmio) throws FarmioException, FarmioFatalException;
}
