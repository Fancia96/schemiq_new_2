package project.schemiq.Service;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import project.schemiq.SchemiqApplication;
import project.schemiq.model.BoardModel;

import project.schemiq.model.ElementModel;
import project.schemiq.model.UserModel;
import project.schemiq.repository.BoardRepository;
import project.schemiq.repository.ElementRepository;
import project.schemiq.repository.UserRepository;
import java.util.Set;
import java.util.Optional;

@Service
public class BoardService {

    private BoardRepository boardRepository;
    private ElementRepository elementRepository;
    private UserRepository userRepository;

    public BoardModel createBoard(BoardModel boardModel) {
        return boardRepository.save(boardModel);
    }

    public BoardModel createBoardWithUserID(BoardModel boardModel, Long userID) {

        Optional<UserModel> user = userRepository.findById(userID);
        if(user.isPresent()){
            UserModel userModel = user.get();
            boardRepository.save(boardModel);
            userModel.getBoardModel().add(boardModel);
            userRepository.save(userModel);
            return boardModel;
        }
        else{
            throw new ObjectNotFoundException(UserService.class, SchemiqApplication.userNotFound);
        }
    }

    public BoardModel updateBoardByID(BoardModel boardModel, Long boardID) {

        Optional<BoardModel> board = boardRepository.findById(boardID);
        if(board.isPresent()){
            BoardModel existingBoard = board.get();
            existingBoard.setBoardName(boardModel.getBoardName());
            return boardRepository.save(existingBoard);
        }
        else{
            throw new ObjectNotFoundException(UserService.class, SchemiqApplication.userNotFound);
        }
    }

    public void deleteBoardByID(Long boardID){
        Optional<BoardModel> board = boardRepository.findById(boardID);
        if(board.isPresent()){
            BoardModel existingBoard = board.get();

            for(ElementModel element : existingBoard.getElementModelList()){
                elementRepository.deleteById(element.getId());
            }
            boardRepository.deleteBoardUsers(existingBoard.getId());
            boardRepository.deleteById(existingBoard.getId());
        }
        else{
            throw new ObjectNotFoundException(UserService.class, SchemiqApplication.userNotFound);
        }
    }

    public BoardModel findBoardByID(Long ID){
        Optional<BoardModel> board = boardRepository.findById(ID);
        if(board.isPresent()){
            return board.get();
        }
        throw new ObjectNotFoundException(UserService.class, SchemiqApplication.elementNotFound);
    }

//    public List<BoardModel> getEverything(){
//        return boardRepository.findAll();
//    }

//    public void deleteAll(){
//        boardRepository.deleteAll();
//    }

    public BoardService(BoardRepository boardRepository, UserRepository userRepository,ElementRepository elementRepository){
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.elementRepository = elementRepository;
    }

//    public BoardModel findOne(BoardModel boardModel) {
//        Optional<BoardModel> board = boardRepository.findById(boardModel.getId());
//        if(board.isPresent()){
//
//            return board.get();
//        }
//        return null;
//    }

}
