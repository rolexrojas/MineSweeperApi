package com.deviget;

import com.deviget.component.MineSweeperComponent;
import com.deviget.types.BoardMoveResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
public class MineSweeperApplication {

	public static void main(String[] args) {

		//SpringApplication.run(MineSweeperApplication.class, args);

		MineSweeperComponent mineSweeperComponent = new MineSweeperComponent();
		mineSweeperComponent.GameSetup();
		BoardMoveResponse boardMoveResponse = mineSweeperComponent.makeMoveAndGetResult(1, 1);
		System.out.println();
		System.out.println(boardMoveResponse.getBoardMoveResponseType().label);
		System.out.println(boardMoveResponse.getBombCellList());
		System.out.println(boardMoveResponse.getEmptyCellList());
		System.out.println(boardMoveResponse.getNumberCellList());
	}

}
