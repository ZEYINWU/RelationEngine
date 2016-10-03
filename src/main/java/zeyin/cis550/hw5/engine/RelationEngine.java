package zeyin.cis550.hw5.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zeyin.cis550.hw5.algebra.IRelationalOperator;
import zeyin.cis550.hw5.algebra.MinusOperator;
import zeyin.cis550.hw5.algebra.NestedLoopsJoinOperator;
import zeyin.cis550.hw5.algebra.ProjectOperator;
import zeyin.cis550.hw5.algebra.RenameOperator;
import zeyin.cis550.hw5.algebra.SelectOperator;
import zeyin.cis550.hw5.algebra.TableScanOperator;
import zeyin.cis550.hw5.data.Relation;
import zeyin.cis550.hw5.data.Tuple;
import zeyin.cis550.hw5.operators.ConstInt;
import zeyin.cis550.hw5.operators.Equals;
import zeyin.cis550.hw5.operators.LessThan;
import zeyin.cis550.hw5.operators.Like;
import zeyin.cis550.hw5.operators.LiteralString;
import zeyin.cis550.hw5.schema.DoubleAttribute;
import zeyin.cis550.hw5.schema.IAttribute;
import zeyin.cis550.hw5.schema.IntAttribute;
import zeyin.cis550.hw5.schema.Schema;
import zeyin.cis550.hw5.schema.StringAttribute;
import zeyin.cis550.hw5.schema.TypeException;

public class RelationEngine {
	Map<String, Relation> relations;
	
	public RelationEngine() {
		relations = new HashMap<String, Relation>();
	}
	
	/**
	 * Initialize schemas
	 */
	public void defineSchemas() {
		Schema actorScheme = new Schema("Actors",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("id"),
						new StringAttribute("first_name"),
						new StringAttribute("last_name"),
						new StringAttribute("gender")
						));
		Relation actors = new Relation(actorScheme);
		
		Schema roleScheme = new Schema("Roles",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("actor_id"),
						new IntAttribute("movie_id"),
						new StringAttribute("role")
						));
		Relation roles = new Relation(roleScheme);
		
		Schema movieScheme = new Schema("Movies",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("id"),
						new StringAttribute("name"),
						new IntAttribute("year"),
						new DoubleAttribute("rank")
						)
				);
		Relation movies = new Relation(movieScheme);

		Schema directorsSchema = new Schema("Directors",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("id"),
						new StringAttribute("first_name"),
						new StringAttribute("last_name")						
						)				
				);
		Relation directors = new Relation(directorsSchema);
		
		Schema director_genreSchema = new Schema("Director_genre",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("director_id"),
						new StringAttribute("genre"),
						new DoubleAttribute("prob")						
						)				
				);
		Relation director_genre = new Relation(director_genreSchema);
		
		Schema movies_directorsSchema = new Schema("Movies_directors",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("director_id"),
						new IntAttribute("movie_id")						
						)			
				);
		Relation movies_directors = new Relation(movies_directorsSchema);
		
		Schema movies_genresSchema = new Schema("Movies_genres",
				Arrays.<IAttribute<?>>asList(
						new IntAttribute("movie_id"),
						new StringAttribute("genre")						
						)			
				);
		Relation movies_genres = new Relation(movies_genresSchema);
		
		relations.put("actors", actors);
		relations.put("roles", roles);
		relations.put("movies", movies);
		relations.put("directors", directors);
		relations.put("director_genre", director_genre);
		relations.put("movies_genres", movies_genres);
		relations.put("movies_directors", movies_directors);
	}
	
	/**
	 * Try to load the tables
	 * 
	 * @param filename Name of the file to import from (assuming SQL format)
	 * @return
	 */
	public boolean loadData(String filename) {
		DataLoader dl = new DataLoader(relations);

		try {
			dl.load(new BufferedReader(new FileReader(new File(filename))));
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Example query with MINUS operation (and select)
	 */
	public void executeMinusQuery() {
		
		TableScanOperator r1 = new TableScanOperator(relations.get("actors"));
		TableScanOperator r2 = new TableScanOperator(relations.get("actors"));
		
		// Select tuples where tuple last_name field < Jones
		SelectOperator sel = new SelectOperator(new LessThan<Tuple,String>(
				Tuple.stringGetter("last_name"),
				new LiteralString("Jones")),
				r2);
		
		MinusOperator minus = new MinusOperator(r1, sel);
		
		IRelationalOperator min = minus;

		System.out.println("Starting MINUS operator");
		long time = System.currentTimeMillis();
		min.initialize();
		
		int i = 0;
		Tuple line = min.getNext();
		while (line != null) {
			i++;
			// Sample the output
			if (i % 1000 == 1)
				System.out.println(line);
			line = min.getNext();
		}
		
		min.shutdown();
		
		System.out.println("Returned " + i + 
				" results in " + (System.currentTimeMillis() - time) + " msec");
		
		System.out.println(min.toString());
	}
	
	/**
	 * Sample query, actors <join> roles with some projection, renaming, and selection
	 * 
	 * @throws TypeException
	 */
	public void executeJoinQuery() throws TypeException {
		IRelationalOperator query;
		
		System.out.println("Starting complex join query");
		
		TableScanOperator act = new TableScanOperator(relations.get("actors"));
		SelectOperator sel = new SelectOperator(new Equals<Tuple,String>(
				Tuple.stringGetter("gender"), new LiteralString("F")), act);
		
		TableScanOperator rol = new TableScanOperator(relations.get("roles"));
		NestedLoopsJoinOperator join = new NestedLoopsJoinOperator(new 
				Equals<Tuple,Integer>(
				Tuple.intGetter("id"),
				Tuple.intGetter("actor_id")
				), sel, rol);
		
		ProjectOperator proj = new ProjectOperator(
				Arrays.asList("first_name", "last_name", "movie_id", "role"),
				join);
		RenameOperator ren = new RenameOperator(
				Arrays.asList("first_name", "last_name", "movie_id", "role"),
				Arrays.asList("first", "last", "movie", "character"),
				proj);

		query = ren;
		
		long time = System.currentTimeMillis();
		query.initialize();
		
		int i = 0;
		Tuple line = query.getNext();
		while (line != null) {
			i++;
			// Sample the output
			if (i % 1000 == 1)
				System.out.println(line);
			line = query.getNext();
		}
		
		query.shutdown();
		
		System.out.println("Finished in " + (System.currentTimeMillis() - time) + " msec");
		System.out.println(query.toString());
	}
	/**
		 * a  SELECT M.name, M.year
					FROM movies M, movies_directors MD, directors D
					WHERE M.id = MD.movie_id and MD.director_id = D.id and D.last_name
					like ’%n’
	 */
	public void executeQueryA1(){
		TableScanOperator moviesTable = new TableScanOperator(relations.get("movies"));
		TableScanOperator movies_directorsTable = new TableScanOperator(relations.get("movies_directors"));
		TableScanOperator directorsTable = new TableScanOperator(relations.get("directors"));
		
		SelectOperator selD = new SelectOperator(new Like<Tuple,String>(
													Tuple.stringGetter("last_name"), new LiteralString("(.*)n(.*)")				
				                  ),directorsTable);
		moviesTable.initialize();
		movies_directorsTable.initialize();
		directorsTable.initialize();
		selD.initialize();
		
		
		int i=0;
		Tuple line = selD.getNext();
		while(line != null){
			i++;
			if(i % 1000 ==1){
				int k = (int)line.getField("id").getValue();
				SelectOperator selMD = new SelectOperator(new Equals<Tuple,Integer>(Tuple.intGetter("director_id"),new ConstInt(k)),
						movies_directorsTable						
						);
				selMD.initialize();
				
				Tuple line2 = selMD.getNext();
				while(line2 != null){
					int movie_id = (int)line2.getField("movie_id").getValue();
					SelectOperator selM = new SelectOperator(new Equals<Tuple,Integer>(Tuple.intGetter("id"),new ConstInt(movie_id)),
							moviesTable);
					
					line2 = selMD.getNext();
					
					selM.initialize();
					Tuple line3 = selM.getNext();
					while(line3 != null){
						String Mname = line3.getField("id").toString();
						String Myear = line3.getField("year").toString();
						System.out.println(Mname+"  "+ Myear);
						
						line3 = selM.getNext();
						
					}
					
					
				}				
			}
			line=selD.getNext();
			
		}			
	}
	
	/**
			 *  SELECT a.id, a.first_name, a.last_name, a.gender
					FROM actors a, roles r, movies_genres mg
					WHERE r.actor_id = a.id and mg.movie_id = r.movie_id and mg.genre = ’Comedy’
			MINUS
					SELECT a.id, a.first_name, a.last_name, a.gender
					FROM actors a, roles r, movies_genres mg
					WHERE r.actor_id = a.id and mg.movie_id =
					r.movie_id and mg.genre = ’Drama’
	 */
	
	
	public void executeQueryB1(){
		TableScanOperator actorsTable = new TableScanOperator(relations.get("actors"));
		TableScanOperator rolesTable = new TableScanOperator(relations.get("roles"));
		TableScanOperator movies_genresTable = new TableScanOperator(relations.get("movies_genres"));
		
		SelectOperator selMG1 = new SelectOperator(new Equals<Tuple,String>(Tuple.stringGetter("genre"),new LiteralString("Comedy")),
				movies_genresTable);
		selMG1.initialize();
		
		
		
		
		SelectOperator selMG2 = new SelectOperator(new Equals<Tuple,String>(Tuple.stringGetter("genre"),new LiteralString("Drama")),
				movies_genresTable);
		selMG2.initialize();
		
		MinusOperator minus = new MinusOperator(selMG1,selMG2);
		minus.initialize();
		
		
		Tuple line = minus.getNext();		
		while(line != null){
			int movie_id = (int) line.getField("movie_id").getValue();
			SelectOperator selR = new SelectOperator(new Equals<Tuple,Integer>(Tuple.intGetter("movie_id"),new ConstInt(movie_id)),rolesTable);
			selR.initialize();
			
			Tuple line2 = selR.getNext();
			while(line2 != null){
				int rActor_id = (int)line2.getField("actor_id").getValue();
				SelectOperator selA = new SelectOperator(new Equals<Tuple,Integer>(Tuple.intGetter("id"),new ConstInt(rActor_id)),actorsTable);
				selA.initialize();
				
				Tuple line3 = selA.getNext();
				while(line3 != null){
								
					System.out.println(line3.toString());
					
					
					line3 = selA.getNext();					
				}				
				line2 = selR.getNext();
			}			
			line = minus.getNext();
		}
		
		
		
	}
	
	/**
	 * Mainline -- initialize the database engine
	 * 
	 * @param args
	 * @throws TypeException
	 */
	public static void main(String[] args) throws TypeException {
		
		RelationEngine engine = new RelationEngine();
		
		engine.defineSchemas();		
		
		// Load the contents of the imdb.sql file
		engine.loadData("imdb.sql");
		
//		engine.executeMinusQuery();
//		
//		engine.executeJoinQuery();
		engine.executeQueryB1();
	}
}
